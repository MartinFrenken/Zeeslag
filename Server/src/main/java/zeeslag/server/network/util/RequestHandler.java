package zeeslag.server.network.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class RequestHandler {

    private RequestHandler() {
    }


    public static void handleRequest(@NotNull HttpServletResponse res, @NotNull Validator validator) throws IOException {
        res.setContentType("application/json");
        RequestResult result = new RequestResult(false);
        try {
            result = validator.validate();
        } catch (Exception ignored) {
        } finally {
            var data = result.getData() == null ? new JsonObject() : result.getData();
            data.addProperty("success", result.isSuccessful());
            res.getWriter().println(data);
        }
    }

}
