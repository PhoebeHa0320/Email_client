package email_client.global;

import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class getSessionIMAPS {
    PropertiesAPI propsAPI = new PropertiesAPI();
    Session emailSession;
    public Session getSession(String imap, String storeType, String user, String password) {
      // create properties field
      Properties properties = new Properties();
      properties.put(propsAPI.getHost(), imap);
      properties.put(propsAPI.getTrustSSL(), imap);
      properties.put(propsAPI.getPort(), "993");
      properties.put(propsAPI.getDebug(), "true");
      properties.put(propsAPI.getStartTLS(), "true");

      // Setup authentication, get session
      emailSession = Session.getInstance(properties,
         new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(user, password);
            }
         });
        return emailSession;        
    }
}
