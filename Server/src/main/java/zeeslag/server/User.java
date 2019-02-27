package zeeslag.server;

import org.jetbrains.annotations.NotNull;

public class User {

    private final String name;
    private final String passwordHash;

    public User(@NotNull String name, @NotNull String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }
}
