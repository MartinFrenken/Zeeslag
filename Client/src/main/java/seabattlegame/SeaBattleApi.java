package seabattlegame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public void get() throws MalformedURLException, IOException
    {
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

            System.out.println(content.toString());

        }
        catch (Exception e)
        {

        }
        finally {
            connection.disconnect();
        }
  }



}
