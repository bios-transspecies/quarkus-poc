package org.acme.dao.adapter.tools;

import io.agroal.api.AgroalDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class DatabaseHelper<T> {

    private final AgroalDataSource dataSource;
    private final Supplier<? extends T> ctor;
    private final Logger log = LoggerFactory.getILoggerFactory().getLogger("DatabaseHelper");
    private final ThreadLocal<Connection> conn = new ThreadLocal<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Inject
    public DatabaseHelper(AgroalDataSource dataSource, Supplier<? extends T> ctor) {
        this.dataSource = dataSource;
        this.ctor = Objects.requireNonNull(ctor);
    }

    public CompletableFuture<Long> save(String sql, List<?> params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                initDbConnection();
                PreparedStatement preparedStatement = conn.get().prepareStatement(sql);
                setParameters(params, preparedStatement);
                preparedStatement.execute();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                long res = 0;
                while(rs.next())
                    res = rs.getLong("id");
                rs.close();
                conn.get().close();
                return res;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public CompletableFuture<T> getOne(String sql, List<?> params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                initDbConnection();
                PreparedStatement preparedStatement = conn.get().prepareStatement(sql);
                setParameters(params, preparedStatement);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) return mapResultSetToT(rs);
                rs.close();
                conn.get().close();
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }, executor);
    }

    public CompletableFuture<List<T>> getList(String sql, List<String> params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                initDbConnection();
                List<T> Ts = new ArrayList<>();
                PreparedStatement preparedStatement = conn.get().prepareStatement(sql);
                setParameters(params, preparedStatement);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) Ts.add(mapResultSetToT(rs));
                rs.close();
                return Ts;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }, executor);
    }

    private T mapResultSetToT(ResultSet rs) {
        T o = ctor.get();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                log.info("mapResultSetToT field: [{}]", field.getName());
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Object value = rs.getObject(field.getName());
                field.set(o, value);
                field.setAccessible(accessible);
            } catch (IllegalAccessException | SQLException e) {
                log(o, e);
            }
        }
        return o;
    }

    private void log(T o, Exception e) {
        log.info(
                "[{}] :: fields in " +
                        "[{}] and columns in related table " +
                        "needs to be exactly the same :: " +
                        "[{}]",
                e.getClass().toString().toUpperCase(),
                o.getClass(),
                e.getMessage());
    }

    private void setParameters(
            List<?> params,
            PreparedStatement preparedStatement) {
        AtomicInteger i = new AtomicInteger(1);
        params.forEach(e -> {
            try {
                String d = e.getClass().getSimpleName();
                switch (d){
                    case "Long":
                        preparedStatement.setLong(i.getAndIncrement(), (Long) e);
                        break;
                    case "Boolean":
                        preparedStatement.setBoolean(i.getAndIncrement(), (Boolean) e);
                        break;
                    case "Integer":
                        preparedStatement.setInt(i.getAndIncrement(), (Integer) e);
                        break;
                    default:
                        preparedStatement.setString(i.getAndIncrement(), String.valueOf(e));
                        break;
                }
            } catch (SQLException ex) {
                log.info(""+ex);
            }
        });
    }

    private void initDbConnection() {
        try {
            Connection connection = conn.get();
            if (connection == null || connection.isClosed())
                conn.set(dataSource.getConnection());
            log.info("initDbConnection [{}] in thread: [{}]",
                    conn.get().hashCode(), Thread.currentThread().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
