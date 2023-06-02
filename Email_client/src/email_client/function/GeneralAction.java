package email_client.function;

import email_client.global.getSessionSMTP;
import javax.mail.MessagingException;
import javax.mail.Session;

public class GeneralAction {
    getSessionSMTP sessionSMTP = new getSessionSMTP();
    
    private String[] getListEmail(String str) {
        if (str.equals("")) {
            return null;
        } else if (!str.contains(",")) {
            String[] strArr = {str};
            return strArr;
        }
        return str.split(",");
    }

    public void SendAction(String username, String pass, String smtp, String portSSL, 
        String touser, String subject, String cc, String bcc, String attachment,String mailField) {        
        String[] listTo = getListEmail(touser.trim());
        String[] listCc = getListEmail(cc.trim());
        String[] listBcc = getListEmail(bcc.trim());                     
        Session sess = sessionSMTP.getSession(username, pass, smtp, portSSL);
        SendMail sender = new SendMail(sess);
        System.out.println(attachment);
        sender.sendEmail(listTo, listCc, listBcc, mailField, attachment, subject, username);
    }
    
    public void FowardAction(String username, String pass, String smtp, String portSSL, 
        String touser, String subject, String cc, String bcc, String attachment,
        String mailField) 
            throws MessagingException {        
        String[] listTo = getListEmail(touser.trim());
        String[] listCc = getListEmail(cc.trim());
        String[] listBcc = getListEmail(bcc.trim());                     
        Session sess = sessionSMTP.getSession(username, pass, smtp, portSSL);
        ForwardMail foward = new ForwardMail(sess);
        System.out.println(attachment);
        foward.forwardEmail(listTo, listCc, listBcc, mailField, attachment, subject, username);
    }
    
    public void ReplyAction(String username, String pass, String smtp, String portSSL, 
        String touser, String subject, String cc, String bcc, String attachment, 
        String mailField) throws MessagingException {        
        String[] listTo = getListEmail(touser.trim());
        String[] listCc = getListEmail(cc.trim());
        String[] listBcc = getListEmail(bcc.trim());                     
        Session sess = sessionSMTP.getSession(username, pass, smtp, portSSL);
        ReplyMail reply = new ReplyMail(sess);
        System.out.println(attachment);
        reply.ReplyEmail(listTo, listCc, listBcc, mailField, attachment, subject, username);
    }
    
}
