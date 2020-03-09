package org.acme.dao.adapter;

import io.agroal.api.AgroalDataSource;
import org.acme.DatabasePort;
import org.acme.dao.adapter.tools.DatabaseHelper;
import org.acme.dao.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class UserDatabaseAccessAdapter implements DatabasePort {

    public static final String SELECT_FROM_DOCKER_USER_WHERE_NAME_ILIKE = "select * from docker.user where name ilike ?";
    public static final String SELECT_FROM_DOCKER_USER = "select * from docker.user";
    public static final String INSERT_INTO_DOCKER_USER_NAME_SURNAME_VALUES = "insert into docker.user (id, name, surname, active, login) values (?, ?, ?, ?, ?)";
    public static final String SELECT_FROM_DOCKER_USER_WHERE_IS = "select * from docker.user where id = ?";
    private final DatabaseHelper<User> dbHelper;

    @Inject
    public UserDatabaseAccessAdapter(AgroalDataSource dataSource) {
        this.dbHelper = new DatabaseHelper<>(dataSource, User::new);
    }

    @Override
    public CompletableFuture<User> findByName(String param) {
        List<String> params = new ArrayList<>();
        params.add(param);
        return dbHelper.getOne(SELECT_FROM_DOCKER_USER_WHERE_NAME_ILIKE, params);
    }

    @Override
    public CompletableFuture<List<User>> all() {
        return dbHelper.getList(SELECT_FROM_DOCKER_USER, new ArrayList<>());
    }

    @Override
    public CompletableFuture<User> add(User user) {
        try {
            Long k = dbHelper.save(INSERT_INTO_DOCKER_USER_NAME_SURNAME_VALUES, getInsertParams(user)).get();
            return getById(k);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CompletableFuture<User> getById(Long id) {
        return dbHelper.getOne(SELECT_FROM_DOCKER_USER_WHERE_IS, getSelectParams(id));
    }

    private List<Long> getSelectParams(Long id) {
        List<Long> selectParams = new ArrayList();
        selectParams.add(id);
        return selectParams;
    }

    private List<?> getInsertParams(User user) {
        List<Object> insertParams = new ArrayList();
        insertParams.add(user.getId());
        insertParams.add(user.getName());
        insertParams.add(user.getSurname());
        insertParams.add(true);
        insertParams.add(user.getSurname());
        return insertParams;
    }
}
