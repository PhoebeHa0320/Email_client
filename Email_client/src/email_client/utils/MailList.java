package email_client.utils;

public class MailList {
    private String from;
    private String Subject;
    private String Date;

    public MailList(String from, String Subject, String Date) {
        this.from = from;
        this.Subject = Subject;
        this.Date = Date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String Subject) {
        this.Subject = Subject;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }
    

    
    
}
