package email_client.global;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
       
    public int CheckConnection(){
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            if (con.getResponseCode() == 200){
                return 1;
            }
        } catch (IOException ex) {     
            ex.printStackTrace();
        }
        return 0;
    }   
}
