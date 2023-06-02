package email_client.utils;

import com.sun.mail.util.BASE64DecoderStream;
import email_client.global.getSessionIMAPS;
import java.io.IOException;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.swing.JEditorPane;

public class FetchContentMail {   
        
   getSessionIMAPS getSession = new getSessionIMAPS();
   String storeType = "imaps";
   public void readEmail(int rowSelected,String imap, String foldername, String user, String password, JEditorPane messagePane ) 
            throws NoSuchProviderException, MessagingException, IOException, ClassCastException {
             
        Session emailSession = getSession.getSession(imap, storeType, user, password);               
        Store store =  emailSession.getStore(storeType);
        store.connect();
        Folder emailFolder = store.getFolder(foldername);
        emailFolder.open(Folder.READ_WRITE);

        Message[] messages = emailFolder.getMessages();
        FetchProfile fetchProfile = new FetchProfile();
        fetchProfile.add(FetchProfile.Item.ENVELOPE);
        emailFolder.fetch(messages, fetchProfile);
        Message msg = messages[rowSelected];

        String contentType = msg.getContentType();
        String messageContent = "";           

        if (contentType.contains("multipart")) {
            Multipart multiPart = (Multipart) msg.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                messageContent = part.getContent().toString();
            }
        }
        else if (contentType.contains("multipart")|| contentType.contains("text/html")) {

            BASE64DecoderStream content =(BASE64DecoderStream) msg.getContent();
            if (content != null) {
                messageContent = content.toString();
           }     
        }

        System.out.println("Nội dung: " + messageContent);
        messagePane.setContentType("text/html");
        messagePane.setText(messageContent);
        emailFolder.close(false);
        store.close();            
    }   
 }


