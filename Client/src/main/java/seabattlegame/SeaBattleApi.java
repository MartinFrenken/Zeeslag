package seabattlegame;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SeaBattleApi
{
    private HttpURLConnection connection;
    private String url;
    public SeaBattleApi(String serverlink)
    {
      url = serverlink;
    }


    public String get() throws IOException
    {
        String output;
        try {
            URL myUrl = new URL(url);
            connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            StringBuilder content;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            output = content.toString();
            System.out.println(content.toString());

        }
        catch (Exception e)
        {
            output = "Error connecting to: "+url+", "+e.toString();
        }
        finally {
            URL myUrl = new URL(url);
            connection = (HttpURLConnection) myUrl.openConnection();
            connection.disconnect();
        }
        return output;
  }


    public void post(String username, String password) throws IOException, UnirestException {
//      User steve = new User(username,password);
//      HttpClient httpclient = HttpClients.createDefault();
//      HttpPost httppost = new HttpPost(url);
//      Gson gson = new Gson();
//      StringEntity postingString = new StringEntity(gson.toJson(steve));
//      httppost.setEntity(postingString);
//      httppost.setHeader("Accept", "application/json");
//      httppost.setHeader("Content-type","application/json");
//      HttpResponse response = httpclient.execute(httppost);
        var json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        HttpResponse<String> response = Unirest.post("http://localhost:3000/login?username=Ruurd")
                .header("cache-control", "no-cache")
                .body(json.toString())
                .asString();
        System.out.println(response.getBody());
  }



}
