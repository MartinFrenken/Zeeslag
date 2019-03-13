package seabattlegame;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SeaBattleApi
{
    private HttpURLConnection connection;
    private String url;
    public SeaBattleApi(String serverlink)
    {
      url = serverlink;
    }
    public String get() throws MalformedURLException, IOException
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
  public void post(String username,String password) throws IOException {
      User steve = new User(username,password);
      HttpClient httpclient = HttpClients.createDefault();
      HttpPost httppost = new HttpPost(url);
      Gson gson = new Gson();
      StringEntity postingString = new StringEntity(gson.toJson(steve));
      httppost.setEntity(postingString);
      httppost.setHeader("Content-type","application/json");
      HttpResponse response = httpclient.execute(httppost);
  }



}
