package zeeslag.server.network.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public final class RequestResult {

    private final boolean success;
    private final JsonObject data;


    public RequestResult(boolean success, JsonObject data) {
        this.success = success;
        this.data = data;
    }


    public RequestResult(boolean success) {
        this.success = success;
        data = null;
    }


    boolean isSuccessful() {
        return success;
    }


    @Nullable JsonObject getData() {
        return data;
    }

}
