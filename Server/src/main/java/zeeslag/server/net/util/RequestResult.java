package zeeslag.server.net.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public final class RequestResult {

    private final boolean success;
    @Nullable
    private final JsonObject data;


    public RequestResult(boolean success, @Nullable JsonObject data) {
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
