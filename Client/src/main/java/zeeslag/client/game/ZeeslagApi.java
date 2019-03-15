package zeeslag.client.game;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

class ZeeslagApi {

    private final String url;


    ZeeslagApi(String url) {
        this.url = url;
    }


    boolean login(String username, String password) {
        try {
            var json = new JsonObject();
            json.addProperty("username", username);
            json.addProperty("password", password);

            return Unirest.post(url + "/login")
                    .body(json.toString())
                    .asJson()
                    .getBody()
                    .getObject()
                    .getBoolean("success");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;
    }

}
