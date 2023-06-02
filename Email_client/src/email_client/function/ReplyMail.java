package email_client.function;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

public class ReplyMail {
    Session sess;
    
    public ReplyMail(Session sess) {
        this.sess = sess;
    }

    public void ReplyEmail(String[] listTo, String[] listCC, String[] listBcc, String content, 
            String FileName, String subject, String from) throws MessagingException {
        InternetAddress[] listEmailTo = convertInnetAddress(listTo);
        InternetAddress[] listEmailCC = convertInnetAddress(listCC);
        InternetAddress[] listEmailBcc = convertInnetAddress(listBcc);     

        MimeMessage mimeMss = new MimeMessage(sess);     
        setSubject(mimeMss, "Reply" + subject);
        setFrom(mimeMss, from);
        setEmailRecipients(mimeMss, listEmailTo, Message.RecipientType.TO);
        setEmailRecipients(mimeMss, listEmailCC, Message.RecipientType.CC);
        setEmailRecipients(mimeMss, listEmailBcc, Message.RecipientType.BCC);

        // Setting cho Body 
        attackEmail(mimeMss, FileName, content);
        sendMess(mimeMss);

    }
    private void attackEmail(MimeMessage mime, String filePath, String content) {
        try 
        {
            // tạo một đối tượng BodyPart để ghi nội dung vào body của Email
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(content);
            // tạo lớp MultyPart để chứa BodyPart
            Multipart mtp = new MimeMultipart();
            mtp.addBodyPart(bodyPart);
            // nếu có File thì tạo thêm một bodyPart khác để chứa file
            if (!filePath.equals("")) {
                MimeBodyPart bodyPart2 = new MimeBodyPart();
                DataSource source = new FileDataSource(filePath);
                bodyPart2.setDataHandler(new DataHandler(source));
                bodyPart2.setFileName(new File(filePath).getName());
                mtp.addBodyPart(bodyPart2);
            }
            mime.setContent(mtp);
        } catch (MessagingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setSubject(MimeMessage mess, String subject) {
        try {
            mess.setSubject(subject);
        } catch (MessagingException e) {
        }
    }
    
     private void setFrom(MimeMessage mess, String from) {
        try {
            mess.setFrom(from);
        } catch (MessagingException e) {
        }
    }

    private void sendMess(MimeMessage mess) {
        try {
            Transport.send(mess);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private InternetAddress[] convertInnetAddress(String[] listTo) {
        if (listTo == null) {
            return null;
        }
        if (listTo.length != 0) {
            InternetAddress[] listAdd = new InternetAddress[listTo.length];
            System.out.println(listTo.length);
            try {
                for (int i = 0; i < listTo.length; i++) {
                    listAdd[i] = new InternetAddress(listTo[i]);
                }
            } catch (AddressException e) {
                JOptionPane.showMessageDialog(null, "Error", "Email Wrong !!", JOptionPane.ERROR_MESSAGE);
            }
            return listAdd;
        }
        return null;
    }

    private void setEmailRecipients(MimeMessage mimeMss, InternetAddress[] listAdd, Message.RecipientType type) {
        try {
            mimeMss.setRecipients(type, listAdd);
        } catch (MessagingException e) {
        }
    }
}
