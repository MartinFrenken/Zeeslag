package zeeslag.client.game;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jetbrains.annotations.Nullable;
import zeeslag.shared.net.UserAuthData;

class ZeeslagApi {

    private final String url;


    ZeeslagApi(String url) {
        this.url = url;
    }


    @Nullable UserAuthData login(String username, String password) {
        try {
            var json = new JsonObject();
            json.addProperty("username", username);
            json.addProperty("password", password);

            var result = Unirest.post(url + "/login")
                    .body(json.toString())
                    .asJson()
                    .getBody()
                    .getObject();
            if (!result.getBoolean("success"))
                return null;
            return new UserAuthData(result.getInt("id"), result.getString("token"));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

}
