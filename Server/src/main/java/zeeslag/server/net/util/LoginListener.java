package zeeslag.server.net.util;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.UserAuthData;

public interface LoginListener {

    int getNewUserId();

    void addToAuthTable(@NotNull UserAuthData authData);

}
