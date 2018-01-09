package com.chernik.internetprovider.context;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class ContextInitializer {
    private final static Logger LOGGER = LogManager.getLogger(ContextInitializer.class);

    private Reflections ref = new Reflections();

    private Map<Class<?>, Object> components = new HashMap<>();
    private Map<Field, Object> withAutowired = new HashMap<>();
    private Map<Method, Object> withAfterCreate = new HashMap<>();
    private Map<Method, Object> withBeforeDestroy = new HashMap<>();

    public Object getComponent(Class<?> clazz) {
        return components.get(getImplementation(clazz));
    }

    public void initialize() {
        LOGGER.log(Level.INFO, "Start initialize context");
        long startTime = System.currentTimeMillis();
        initComponents(Component.class);
        initComponents(Repository.class);
        initComponents(Service.class);
        initComponents(HttpRequestProcessor.class);
        autowireComponents();
        LOGGER.log(Level.DEBUG, "Start after create methods");
        invokeLifeCycleMethod(withAfterCreate);
        long stopTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Context was initialize in {} milliseconds", stopTime - startTime);
    }

    public void destroy() {
        LOGGER.log(Level.INFO, "Start destroy context");
        LOGGER.log(Level.DEBUG, "Start before destroy methods");
        invokeLifeCycleMethod(withBeforeDestroy);
        LOGGER.log(Level.INFO, "Context was destroy");
    }


    private void initComponents(Class<? extends Annotation> annotation) {
        LOGGER.log(Level.TRACE, "Finding class with annotation {}", annotation);
        ref.getTypesAnnotatedWith(annotation)
                .forEach(clazz -> {
                    Object component = createInstance(clazz);
                    addAutowireField(clazz, component);
                    addLifeCycleMethod(clazz, component);
                    components.put(clazz, component);
                });
    }

    private Object createInstance(Class<?> clazz) {
        LOGGER.log(Level.TRACE, "Creating instance of class {}", clazz);
        Object component;
        try {
            Constructor constructor = clazz.getConstructor();
            component = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            String message = String.format("Can't resolved constructor with zero parameters for class %s", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        } catch (IllegalAccessException e) {
            String message = String.format("Can't create class %s. Constructor should be public", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        } catch (InstantiationException e) {
            String message = String.format("Can't create class %s. Class should not be abstract", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        } catch (InvocationTargetException e) {
            String message = String.format("Can't create class %s. Exception in constructor", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        }

        return component;
    }

    private void addAutowireField(Class<?> clazz, Object component) {
        Reflections autowiredRef = new Reflections(clazz.getCanonicalName(), new FieldAnnotationsScanner());
        autowiredRef.getFieldsAnnotatedWith(Autowired.class)
                .forEach(field -> withAutowired.put(field, component));
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            addAutowireField(superClazz, component);
        }
    }

    private void addLifeCycleMethod(Class<?> clazz, Object component) {
        Reflections afterCreateRef = new Reflections(clazz.getCanonicalName(), new MethodAnnotationsScanner());
        afterCreateRef.getMethodsAnnotatedWith(AfterCreate.class)
                .forEach(method -> withAfterCreate.put(method, component));
        afterCreateRef.getMethodsAnnotatedWith(BeforeDestroy.class)
                .forEach(method -> withBeforeDestroy.put(method, component));
    }


    private void autowireComponents() {
        LOGGER.log(Level.TRACE, "Autowiring components to fields");
        withAutowired.forEach((field, value) -> {
            try {
                field.setAccessible(true);
                Object settingValue = getComponentOrImpemetation(field.getType());
                field.set(value, settingValue);
            } catch (IllegalAccessException e) {
                String message = String.format("Error with access to field %s", field);
                LOGGER.log(Level.FATAL, message);
                throw new RuntimeException(message, e);
            } finally {
                field.setAccessible(false);
            }
        });
    }


    private void invokeLifeCycleMethod(Map<Method, Object> lifeCycle) {
        lifeCycle.forEach((method, value) -> {
            Parameter[] inputParameters = method.getParameters();
            List<Object> parameters = new ArrayList<>();
            Arrays.stream(inputParameters).forEach(inputParameter -> {
                Object parameter;
                if (List.class.isAssignableFrom(inputParameter.getType())) {
                    parameter = getAsList(inputParameter.getParameterizedType());
                } else {
                    parameter = getComponentOrImpemetation(inputParameter.getType());
                }
                parameters.add(parameter);
            });
            try {
                method.invoke(value, parameters.toArray());
            } catch (IllegalAccessException e) {
                String message = String.format("Can't invoke method %s. Method should be public", method);
                LOGGER.log(Level.FATAL, message);
                throw new RuntimeException(message, e);
            } catch (InvocationTargetException e) {
                String message = String.format("Can't invoke method %s. Exception in method", method);
                LOGGER.log(Level.FATAL, message);
                throw new RuntimeException(message, e);
            }
        });
    }

    private List<Object> getAsList(Type type) {
        List<Object> parameters = new ArrayList<>();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> genericType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        ref.getSubTypesOf(genericType).stream().map(clazz ->
                components.get(clazz)).forEach(parameter -> {
            if (parameter != null) {
                parameters.add(parameter);
            }
        });
        return parameters;
    }

    private Object getComponentOrImpemetation(Class<?> clazz) {
        Object parameter = components.get(clazz);
        if (parameter == null) {
            parameter = components.get(getImplementation(clazz));
        }
        return parameter;
    }


    private Class<?> getImplementation(Class<?> annotation) {
        Set classSet = ref.getSubTypesOf(annotation);
        return (Class<?>) classSet.toArray()[0];
    }
}
