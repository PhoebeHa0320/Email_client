package email_client.global;

public class folderMailName {
    public final String inboxFolder = "INBOX"; //folder INBOX dùng chung cho tất cả dịch vụ mail
    //Gmail
    public final String sentGmail = "[Gmail]/Thư đã gửi";
    public final String spamGmail = "[Gmail]/Thư rác";
    public final String trashGmail = "[Gmail]/Thùng rác";
    //outlook
    public final String sentOutlook = "Sent";
    public final String spamOutlook = "Junk";
    public final String trashOutlook = "Deleted";
    //Yahoo
    public final String sentYahoo = "Sent";
    //public final String spamYahoo = "NOT SUPPORT"; //hiện tại javamail không tìm đc folder thư rác của yahoo mail
    public final String trashYahoo = "Trash";

    public String getInboxFolder() {
        return inboxFolder;
    }

    public String getSentGmail() {
        return sentGmail;
    }

    public String getSpamGmail() {
        return spamGmail;
    }

    public String getTrashGmail() {
        return trashGmail;
    }

    public String getSentOutlook() {
        return sentOutlook;
    }

    public String getSpamOutlook() {
        return spamOutlook;
    }

    public String getTrashOutlook() {
        return trashOutlook;
    }

    public String getSentYahoo() {
        return sentYahoo;
    }

//    public String getSpamYahoo() {
//        return spamYahoo;
//    }

    public String getTrashYahoo() {
        return trashYahoo;
    }
}
