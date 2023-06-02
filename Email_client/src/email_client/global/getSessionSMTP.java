package email_client.global;

import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class getSessionSMTP {    
    PropertiesAPI propertiesAPI = new PropertiesAPI();
    public Session getSession(String username, String password, String smtp, String portSSL) {
        Properties props = new Properties();
        props.put(propertiesAPI.getSmtpAuth(), "true");
        props.put(propertiesAPI.getSmtpHost(), smtp);        
        props.put(propertiesAPI.getSmtpPort(), portSSL);
        props.put(propertiesAPI.getSmtpTLS(), "true"); //enable STARTTLS
        props.put(propertiesAPI.getDebug(), "true");       
        Session session = Session.getInstance(props,
                 new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication(username, password);
                    }
                 });
        return session;       
    }
}
