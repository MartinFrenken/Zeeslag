package zeeslag.server.net;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.UserAuthData;

interface LoginListener {

    int getNewUserId();

    void addToAuthMap(@NotNull UserAuthData authData);

}
