package zeeslag.server.network;

import com.google.gson.JsonParser;
import zeeslag.server.network.util.RequestHandler;
import zeeslag.server.network.util.RequestResult;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestHandler.handleRequest(res, () -> {
            var body = new JsonParser().parse(req.getReader()).getAsJsonObject();
            var username = body.get("username").getAsString();
            var password = body.get("password").getAsString();

            if (!username.equals("Ruurd")) return new RequestResult(false);
            if (!password.equals("Ben123")) return new RequestResult(false);

            return new RequestResult(true);
        });
    }

}
