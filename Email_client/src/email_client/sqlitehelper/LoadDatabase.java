package email_client.sqlitehelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class LoadDatabase {
    Connection connection = sqlitehelper.getConnection();
    public void loaddatabase() {
        //bỏ pop3, sử dụng loại imap
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec          
            statement.executeUpdate("create table email (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NULL, "    //tên cho tài khoản email (Không bắt buộc)
                    + "email TEXT NOT NULL, " //địa chỉ tài khoản email
                    + "password TEXT NOT NULL, " //mật khẩu
                    + "type TEXT NOT NULL, " //loại (imap hay pop3) incoming messeage --> không sử dụng pop3
                    + "server TEXT NOT NULL, " //gmail, outlook và yahoo (hiện tại thế này đã)
                    + "smtp TEXT NOT NULL,"   //smtp server của email outgoing messeage
                    + "imap TEXT NOT NULL,"   //imap server của email incoming messeage
                    + "portTLS TEXT NOT NULL," //port của stmp server
                    + "portSSL TEXT NOT NULL , "   //port của stmp server
                    + "portIMAP TEXT NOT NULL )");   //port của IMAP server
        } catch (SQLException ex) {
            //Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
            //bỏ qua cái exception này nếu bảng tồn tại thôi          
        }
    }
}
