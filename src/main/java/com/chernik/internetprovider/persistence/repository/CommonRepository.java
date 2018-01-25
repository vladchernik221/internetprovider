package com.chernik.internetprovider.persistence.repository;

import com.chernik.internetprovider.context.Autowired;
import com.chernik.internetprovider.context.Repository;
import com.chernik.internetprovider.exception.DatabaseException;
import com.chernik.internetprovider.exception.TimeOutException;
import com.chernik.internetprovider.persistence.ConnectionPool;
import com.chernik.internetprovider.persistence.Page;
import com.chernik.internetprovider.persistence.Pageable;
import com.chernik.internetprovider.persistence.repository.util.BiThrowableFunction;
import com.chernik.internetprovider.persistence.repository.util.ThrowableFunction;
import com.chernik.internetprovider.persistence.repository.util.TriThrowableFunction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CommonRepository {
    private static final Logger LOGGER = LogManager.getLogger(CommonRepository.class);

    @Autowired
    private ConnectionPool connectionPool;

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    public <T> Long create(T entity, BiThrowableFunction<Connection, T, PreparedStatement> statementFunctional) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Inserting  {}", entity);
        Connection connection = connectionPool.getConnection();
        Long generatedId;
        try (PreparedStatement statement = statementFunctional.apply(connection, entity)) {
            statement.executeUpdate();
            generatedId = getGeneratedId(statement);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Inserting complete successful");
        return generatedId;
    }

    public <T, E> Long create(T firstParameter, E secondParameter, TriThrowableFunction<Connection, T, E, PreparedStatement> statementFunctional) throws DatabaseException, TimeOutException {
        Connection connection = connectionPool.getConnection();
        Long generatedId;
        try (PreparedStatement statement = statementFunctional.apply(connection, firstParameter, secondParameter)) {
            statement.executeUpdate();
            generatedId = getGeneratedId(statement);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Inserting complete successful");
        return generatedId;
    }

    public <T> void executeUpdate(T entity, BiThrowableFunction<Connection, T, PreparedStatement> statementFunctional) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Updating {}", entity);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = statementFunctional.apply(connection, entity)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Updating complete successful");
    }

    public <T, E> void executeUpdate(T first, E second, TriThrowableFunction<Connection, T, E, PreparedStatement> statementFunctional) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Updating {}, {}", first, second);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = statementFunctional.apply(connection, first, second)) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Updating complete successful");
    }

    public <T, E> void executeBatch(T first, E second, TriThrowableFunction<Connection, T, E, PreparedStatement> statementFunctional) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Inserting {}, {}", first, second);
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = statementFunctional.apply(connection, first, second)) {
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Updating complete successful");
    }

    public <T> boolean exist(T parameter, BiThrowableFunction<Connection, T, PreparedStatement> statementFunctional) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing by {}", parameter);
        boolean isExist;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = statementFunctional.apply(connection, parameter);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            } else {
                throw new DatabaseException("Error while execute database query");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Existing is {}", isExist);
        return isExist;
    }

    public <T, E> boolean exist(T firstParameter, E secondParameter, TriThrowableFunction<Connection, T, E, PreparedStatement> statementFunctional) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Check existing by {} and {}", firstParameter, secondParameter);
        boolean isExist;
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = statementFunctional.apply(connection, firstParameter, secondParameter);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            } else {
                throw new DatabaseException("Error while execute database query");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Existing is {}", isExist);
        return isExist;
    }

    public <T, E> Optional<T> getByParameters(E parameter, BiThrowableFunction<Connection, E, PreparedStatement> statementFunctional, ThrowableFunction<ResultSet, T> entityCreateFunction) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting by {}", parameter);
        Connection connection = connectionPool.getConnection();
        T entity = null;
        try (PreparedStatement statement = statementFunctional.apply(connection, parameter);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                entity = entityCreateFunction.apply(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting complete successful");
        return Optional.ofNullable(entity);
    }

    public <T, E, U> Optional<T> getByParameters(E firstParameter, U secondParameter, TriThrowableFunction<Connection, E, U, PreparedStatement> statementFunctional, ThrowableFunction<ResultSet, T> entityCreateFunction) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting by {}", firstParameter);
        Connection connection = connectionPool.getConnection();
        T entity = null;
        try (PreparedStatement statement = statementFunctional.apply(connection, firstParameter, secondParameter);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                entity = entityCreateFunction.apply(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting complete successful");
        return Optional.ofNullable(entity);
    }

    public <T> Page<T> getPage(Pageable pageable, BiThrowableFunction<Connection, Pageable, PreparedStatement> countStatementFunctional, BiThrowableFunction<Connection, Pageable, PreparedStatement> statementFunctional, ThrowableFunction<ResultSet, T> createEntityFunction) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page. Page number is {}, page size is {}", pageable.getPageNumber(), pageable.getPageSize());
        Connection connection = connectionPool.getConnection();
        Page<T> entityPage = new Page<>();
        try (PreparedStatement countStatement = countStatementFunctional.apply(connection, pageable);
             ResultSet countResultSet = countStatement.executeQuery();
             PreparedStatement statement = statementFunctional.apply(connection, pageable);
             ResultSet resultSet = statement.executeQuery()) {

            if (countResultSet.next()) {
                entityPage.setPagesCount(countResultSet.getInt(1));
            } else {
                throw new DatabaseException("Getting pages count failed");
            }

            List<T> entities = new ArrayList<>();
            while (resultSet.next()) {
                entities.add(createEntityFunction.apply(resultSet));
            }

            entityPage.setData(entities);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting complete successful");
        return entityPage;
    }

    public <T, E> Page<T> getPage(E parameter, Pageable pageable, TriThrowableFunction<Connection, E, Pageable, PreparedStatement> countStatementFunctional, TriThrowableFunction<Connection, E, Pageable, PreparedStatement> statementFunctional, ThrowableFunction<ResultSet, T> createEntityFunction) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting page. Page number is {}, page size is {}", pageable.getPageNumber(), pageable.getPageSize());
        Connection connection = connectionPool.getConnection();
        Page<T> entityPage = new Page<>();
        try (PreparedStatement countStatement = countStatementFunctional.apply(connection, parameter, pageable);
             ResultSet countResultSet = countStatement.executeQuery();
             PreparedStatement statement = statementFunctional.apply(connection, parameter, pageable);
             ResultSet resultSet = statement.executeQuery()) {

            if (countResultSet.next()) {
                entityPage.setPagesCount(countResultSet.getInt(1));
            } else {
                throw new DatabaseException("Getting count failed");
            }

            List<T> entities = new ArrayList<>();
            while (resultSet.next()) {
                entities.add(createEntityFunction.apply(resultSet));
            }

            entityPage.setData(entities);
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting complete successful");
        return entityPage;
    }

    public <T> List<T> getAll(ThrowableFunction<Connection, PreparedStatement> statementFunctional, ThrowableFunction<ResultSet, T> createEntityFunction) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting all");
        Connection connection = connectionPool.getConnection();
        List<T> entities = new ArrayList<>();
        try (PreparedStatement statement = statementFunctional.apply(connection);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                entities.add(createEntityFunction.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting complete successful");
        return entities;
    }

    public <T, C> List<T> getAllByСondition(C condition, BiThrowableFunction<Connection, C, PreparedStatement> statementFunctional, ThrowableFunction<ResultSet, T> createEntityFunction) throws DatabaseException, TimeOutException {
        LOGGER.log(Level.TRACE, "Getting all");
        Connection connection = connectionPool.getConnection();
        List<T> entities = new ArrayList<>();
        try (PreparedStatement statement = statementFunctional.apply(connection, condition);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                entities.add(createEntityFunction.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        LOGGER.log(Level.TRACE, "Getting complete successful");
        return entities;
    }

    private Long getGeneratedId(PreparedStatement statement) throws DatabaseException {
        Long generatedId;
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getLong(1);
            } else {
                throw new DatabaseException("Getting generated key failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while execute database query, when get generated key", e);
        }
        LOGGER.log(Level.TRACE, "Generated id: {}", generatedId);
        return generatedId;
    }
}
