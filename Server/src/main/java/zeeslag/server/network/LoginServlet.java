package zeeslag.server.network;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class LoginServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        var test = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        res.setContentType("application/json");
        res.setHeader("Accept", "application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
        if (username.equals("Ruurd")) {
            res.getWriter().println("{\"success\":true}" + test);
            System.out.println("Hello");
            return;
        }
        res.getWriter().println("{\"success\":false}");
    }

}
