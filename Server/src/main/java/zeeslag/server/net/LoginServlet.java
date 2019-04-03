package zeeslag.server.net;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import zeeslag.server.net.util.RequestHandler;
import zeeslag.server.net.util.RequestResult;
import zeeslag.shared.net.UserAuthData;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class LoginServlet extends HttpServlet {

    private final LoginListener loginListener;


    public LoginServlet(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    @Override
    protected void doPost(@NotNull HttpServletRequest req, @NotNull HttpServletResponse res) throws IOException {
        RequestHandler.handleRequest(res, () -> {
            var body = new JsonParser().parse(req.getReader()).getAsJsonObject();
            var username = body.get("username").getAsString();
            var password = body.get("password").getAsString();

            if (!username.equals("Q")) return new RequestResult(false);
            if (!password.equals("W")) return new RequestResult(false);

            var token = UUID.randomUUID().toString();
            var authData = new UserAuthData(loginListener.getNewUserId(), token);
            loginListener.addToAuthMap(authData);
            return new RequestResult(true, new Gson().toJsonTree(authData).getAsJsonObject());
        });
    }

}
