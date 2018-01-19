package com.chernik.internetprovider.context;

import com.chernik.internetprovider.persistence.TransactionManager;
import com.chernik.internetprovider.persistence.TransactionalConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Uses for initializing and configuration application context. Create objects of all classes annotated as
 * <code>@Component</code>, <code>@Repository</code>, <code>@Service</code> or <code>@HttpRequestProcessor</code>, set
 * appropriate of them to fields annotated as <code>@Autowired</code>, invoke methods of object life cycle.
 *
 * @see Autowired
 * @see Component
 * @see Repository
 * @see Service
 * @see HttpRequestProcessor
 */
public class ContextInitializer {
    private static final Logger LOGGER = LogManager.getLogger(ContextInitializer.class);

    /**
     * Uses for working with Java Reflection API.
     */
    private Reflections ref = new Reflections();

    /**
     * Contains one object of each class annotated as <code>@Component</code>, <code>@Repository</code>,
     * <code>@Service</code> or <code>@HttpRequestProcessor</code>. Key is class of object, value is created object.
     *
     * @see Component
     * @see Repository
     * @see Service
     * @see HttpRequestProcessor
     */
    private Map<Class<?>, Object> components = new HashMap<>();

    /**
     * Contains information for injecting objects via <code>@Autowired</code>. Each element of list is pair of field,
     * annotated as <code>@Autowired</code>, and object, that contains this field.
     *
     * @see Autowired
     */
    private List<Map.Entry<Field, Object>> withAutowired = new ArrayList<>();

    /**
     * Contains information for invoking methods of life cycle, that should be invoked after creating object. Each
     * element of list is pair of method, annotated as <code>@AfterCreate</code>, and object, that contains this method.
     *
     * @see AfterCreate
     */
    private List<Map.Entry<Method, Object>> withAfterCreate = new ArrayList<>();

    /**
     * Contains information for invoking methods of life cycle, that should be invoked before destroying object. Each
     * element of list is pair of method, annotated as <code>@BeforeDestroy</code>, and object, that contains this
     * method.
     *
     * @see BeforeDestroy
     */
    private List<Map.Entry<Method, Object>> withBeforeDestroy = new ArrayList<>();

    /**
     * Gets component by its class.
     *
     * @param clazz class of needed component.
     * @return instance of specified class.
     */
    public Object getComponent(Class<?> clazz) {
        return getComponentOrImplementation(clazz);
    }


    /**
     * Initialize context: fill map of components, inject them to appropriate fields annotated as
     * {@link Autowired}, invoke methods annotated as {@link AfterCreate}.
     */
    public void initialize() {
        LOGGER.log(Level.INFO, "Start initialize context");
        long startTime = System.currentTimeMillis();
        initComponents(Component.class);
        initComponents(Repository.class);
        initProxyServices(Service.class);
        initComponents(HttpRequestProcessor.class);
        autowireComponents();
        LOGGER.log(Level.DEBUG, "Start after createOrUpdate methods");
        invokeLifeCycleMethod(withAfterCreate);
        long stopTime = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Context was initialize in {} milliseconds", stopTime - startTime);
    }

    private void initProxyServices(Class<Service> annotation) {
        LOGGER.log(Level.TRACE, "Search class with annotation {} for creating proxy", annotation);
        ref.getTypesAnnotatedWith(annotation)
                .forEach(clazz -> {
                    Object component = createInstance(clazz);
                    addAutowireField(clazz, component);
                    addLifeCycleMethod(clazz, component);
                    component = createProxyOfClass(clazz, component);
                    components.put(clazz, component);
                });
    }

    private Object createProxyOfClass(Class<?> clazz, Object component) {
        Class<?> anInterface = clazz.getInterfaces()[0];
        ServiceInvocationHandler serviceInvocationHandler = new ServiceInvocationHandler((TransactionalConnectionPool) getComponent(TransactionManager.class), component);
        return Proxy.newProxyInstance(anInterface.getClassLoader(), new Class[]{anInterface}, serviceInvocationHandler);
    }

    /**
     * Destroy context: invoke methods annotated as {@link BeforeDestroy}.
     */
    public void destroy() {
        LOGGER.log(Level.INFO, "Start destroy context");
        LOGGER.log(Level.DEBUG, "Start before destroy methods");
        invokeLifeCycleMethod(withBeforeDestroy);
        LOGGER.log(Level.INFO, "Context was destroy");
    }

    /**
     * Create instances of components annotated as <code>annotation</code>. Search all classes with specified
     * annotation, createOrUpdate them instances, search all fields of this component annotated as {@link Autowired} and
     * all methods of life cycle.
     *
     * @param annotation specify annotation, components with that should be initialized.
     */
    private void initComponents(Class<? extends Annotation> annotation) {
        LOGGER.log(Level.TRACE, "Search class with annotation {}", annotation);
        ref.getTypesAnnotatedWith(annotation)
                .forEach(clazz -> {
                    Object component = createInstance(clazz);
                    addAutowireField(clazz, component);
                    addLifeCycleMethod(clazz, component);
                    components.put(clazz, component);
                });
    }

    /**
     * Create instance of class <code>clazz</code> via constructor without parameters.
     *
     * @param clazz specify class, instance of that should be created.
     * @return created instance of <code>clazz</code>.
     * @throws RuntimeException if constructor without parameters can't be resolve or throw exception, or if target
     *                          class is abstract or not public.
     */
    private Object createInstance(Class<?> clazz) {
        LOGGER.log(Level.TRACE, "Creating instance of class {}", clazz);
        Object component;
        try {
            Constructor constructor = clazz.getConstructor();
            component = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            String message = String.format("Can't resolve constructor with zero parameters for class %s", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        } catch (IllegalAccessException e) {
            String message = String.format("Can't createOrUpdate class %s. Constructor should be public", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        } catch (InstantiationException e) {
            String message = String.format("Can't createOrUpdate class %s. Class should not be abstract", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        } catch (InvocationTargetException e) {
            String message = String.format("Can't createOrUpdate class %s. Exception in constructor", clazz);
            LOGGER.log(Level.FATAL, message);
            throw new RuntimeException(message, e);
        }

        return component;
    }

    /**
     * Search fields in component annotated as {@link Autowired} and add them to {@link #withAutowired} list.
     *
     * @param clazz     class of component for search fields.
     * @param component instance of component for injection.
     */
    private void addAutowireField(Class<?> clazz, Object component) {
        Reflections autowiredRef = new Reflections(clazz.getCanonicalName(), new FieldAnnotationsScanner());
        autowiredRef.getFieldsAnnotatedWith(Autowired.class)
                .forEach(field -> withAutowired.add(new AbstractMap.SimpleEntry<>(field, component)));
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            addAutowireField(superClazz, component);
        }
    }

    /**
     * Search methods of life cycle in component annotated as {@link AfterCreate} or {@link BeforeDestroy} and add them
     * to appropriate lists: {@link #withAfterCreate} and {@link #withBeforeDestroy}.
     *
     * @param clazz     class of component for search fields.
     * @param component instance of component for injection.
     */
    private void addLifeCycleMethod(Class<?> clazz, Object component) {
        Reflections afterCreateRef = new Reflections(clazz.getCanonicalName(), new MethodAnnotationsScanner());
        afterCreateRef.getMethodsAnnotatedWith(AfterCreate.class)
                .forEach(method -> withAfterCreate.add(new AbstractMap.SimpleEntry<>(method, component)));
        afterCreateRef.getMethodsAnnotatedWith(BeforeDestroy.class)
                .forEach(method -> withBeforeDestroy.add(new AbstractMap.SimpleEntry<>(method, component)));
    }

    /**
     * Inject appropriate instances of components to fields from {@link #withAutowired} list. Created components are got
     * from {@link #components} map.
     *
     * @throws RuntimeException if target field is not accessible.
     */
    private void autowireComponents() {
        LOGGER.log(Level.TRACE, "Autowiring components to fields");
        withAutowired.forEach((entry) -> {
            try {
                entry.getKey().setAccessible(true);
                Object settingValue = getComponentOrImplementation(entry.getKey().getType());
                entry.getKey().set(entry.getValue(), settingValue);
            } catch (IllegalAccessException e) {
                String message = String.format("Error with access to field %s", entry.getKey());
                LOGGER.log(Level.FATAL, message);
                throw new RuntimeException(message, e);
            } finally {
                entry.getKey().setAccessible(false);
            }
        });
    }

    /**
     * Invoke life cycle methods for components from specified map. If this method has parameters, they will be inject
     * from {@link #components} map according to class of parameter.
     *
     * @param lifeCycle list of pairs: life cycle method that should be invoked, instance of component for that should
     *                  be invoked.
     */
    private void invokeLifeCycleMethod(List<Map.Entry<Method, Object>> lifeCycle) {
        lifeCycle.forEach((entry) -> {
            Parameter[] inputParameters = entry.getKey().getParameters();
            List<Object> parameters = new ArrayList<>();
            Arrays.stream(inputParameters).forEach(inputParameter -> {
                Object parameter;
                if (List.class.isAssignableFrom(inputParameter.getType())) {
                    parameter = getAsList(inputParameter.getParameterizedType());
                } else {
                    parameter = getComponentOrImplementation(inputParameter.getType());
                }
                parameters.add(parameter);
            });
            try {
                entry.getKey().invoke(entry.getValue(), parameters.toArray());
            } catch (IllegalAccessException e) {
                String message = String.format("Can't invoke method %s. Method should be public", entry.getKey());
                LOGGER.log(Level.FATAL, message);
                throw new RuntimeException(message, e);
            } catch (InvocationTargetException e) {
                String message = String.format("Can't invoke method %s. Exception in method", entry.getKey());
                LOGGER.log(Level.FATAL, message);
                throw new RuntimeException(message, e);
            }
        });
    }

    /**
     * Get all components of appropriate type as <code>List</code>. Appropriate type means exactly of this class or
     * subclasses.
     *
     * @param type type of components, that should be got as list
     * @return list of found components
     */
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

    /**
     * Get component of <code>clazz</code> if it's class, or component of class, that implements <code>clazz</code> if
     * it's interface.
     *
     * @param clazz class for searching
     * @return component appropriated specified class
     */
    private Object getComponentOrImplementation(Class<?> clazz) {
        Object parameter = components.get(clazz);
        if (parameter == null) {
            parameter = components.get(getImplementation(clazz));
        }
        return parameter;
    }

    /**
     * Get class, that implements specified interface.
     *
     * @param interfaceClass class of interface for searching.
     * @return one class, that implements specified interface.
     */
    private Class<?> getImplementation(Class<?> interfaceClass) {
        Set<? extends Class<?>> classSet = ref.getSubTypesOf(interfaceClass);
        Optional<? extends Class<?>> implementation = classSet.stream().filter(clazz -> clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class) || clazz.isAnnotationPresent(HttpRequestProcessor.class)).findFirst();

        if (implementation.isPresent()) {
            return implementation.get();
        } else {
            throw new ConcurrentModificationException(String.format("Interface %s hasn't implementation", interfaceClass));
        }
    }
}
