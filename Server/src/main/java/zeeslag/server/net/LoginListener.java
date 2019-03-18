package zeeslag.server.net;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.UserAuthData;

public interface LoginListener {

    int getNewUserId();

    void addToAuthMap(@NotNull UserAuthData authData);

}
