package zeeslag.server.net.util;

import org.jetbrains.annotations.NotNull;

public interface Validator {

    @NotNull RequestResult validate() throws Exception;

}
