package zeeslag.shared.net;

import org.jetbrains.annotations.NotNull;

public class UserAuthData {

    public final int id;
    @NotNull
    public final String token;


    public UserAuthData(int id, @NotNull String token) {
        this.id = id;
        this.token = token;
    }

}
