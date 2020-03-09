package org.acme;

import org.acme.dao.user.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class UserService {

    private final DatabasePort databaseAdapter;

    @Inject
    public UserService(DatabasePort databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    public CompletableFuture<User> fromService(String param)
            throws ExecutionException, InterruptedException
    {
        CompletableFuture<User> byName = databaseAdapter.findByName(param);
        CompletableFuture<Void> as = byName.thenAcceptAsync(a -> a.setName(a.getId() + a.getName()));
         as.get();
        return byName;
    }

    public CompletableFuture<List<User>> all() {
        return databaseAdapter.all();
    }

    public CompletableFuture<User> post(User user) {
        return databaseAdapter.add(user);
    }
}
