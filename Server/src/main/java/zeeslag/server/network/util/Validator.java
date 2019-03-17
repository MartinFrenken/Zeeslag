package zeeslag.server.network.util;

import org.jetbrains.annotations.NotNull;

public interface Validator {

    @NotNull RequestResult validate() throws Exception;

}
