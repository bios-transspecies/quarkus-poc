package org.acme;

import org.acme.dao.user.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DatabasePort {

    CompletableFuture<User> findByName(String param);

    CompletableFuture<List<User>> all();

    CompletableFuture<User> add(User user);
}
