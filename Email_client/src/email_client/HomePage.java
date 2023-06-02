package email_client;

import email_client.callFrame.frameManageAccount;
import email_client.callFrame.frameAboutSoftware;
import email_client.callFrame.frameAboutTeam;
import email_client.callFrame.frameAddAccount;
import email_client.callFrame.frameSendEmail;
import email_client.dialogMess.DownloadMailMesseage;
import email_client.global.LookandFeel;
import email_client.global.IconImageUtilities;
import email_client.dialogMess.NetworkNotify;
import email_client.dialogMess.loadContentFailed;
import email_client.function.DeleteMail;
import email_client.global.folderMailName;
import email_client.global.macOS.mainForm;
import email_client.sqlitehelper.sqlitehelper;
import email_client.utils.gmail.GmailSent;
import email_client.utils.gmail.GmailSpam;
import email_client.utils.gmail.GmailTrash;
import email_client.utils.FetchContentMail;
import email_client.utils.FetchInbox;
import email_client.utils.FetchContentPlainText;
import email_client.utils.SearchMail;
import email_client.utils.outlook.OutlookSent;
import email_client.utils.outlook.OutlookSpam;
import email_client.utils.outlook.OutlookTrash;
import email_client.utils.yahoo.YahooSent;
import email_client.utils.yahoo.YahooSpam;
import email_client.utils.yahoo.YahooTrash;
import java.awt.Dialog;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class HomePage extends javax.swing.JFrame {
    //<editor-fold defaultstate="collapsed" desc="Global Function">
    DownloadMailMesseage downloadDialog = new DownloadMailMesseage(this, true);
    mainForm main = new mainForm();
    folderMailName foldername = new folderMailName();
    frameSendEmail sendGUI = new frameSendEmail();    
    Connection connection = sqlitehelper.getConnection();
    DefaultTableModel model ;
    PreparedStatement ps;
    ResultSet rs;     
    String imap, storeType, user, fromUser, password, server, date, subject, mailContent, foldermailname, mailsearchInput;
    int rowSelected;
    //imap chính là server kết nối của dịch vụ email
    //storeType: pop3 - mail lưu cục bộ, imap: mail ko lưu cục bộ, giao tiếp với server
    //user: chính là email
    //pass: khỏi cần giải thích :)
    //mailfoldername: đây là một trick dành cho đọc nội dung mail
    //////////////////////////////////////////////////////////////////////////////////////////////
    //gọi utils fetch email   
    FetchInbox fetchInbox = new FetchInbox();
    //Tài khoản Gmail   
    GmailSent sentGmail = new GmailSent();
    GmailSpam spamGmail = new GmailSpam();
    GmailTrash trashGmail = new GmailTrash();
    //Tài khoản Outlook, Office 365   
    OutlookSent sentOutlook = new OutlookSent();
    OutlookSpam spamOutlook = new OutlookSpam();
    OutlookTrash trashOutlook = new OutlookTrash();
    //Tài khoản Yahoo Mail   
    YahooSent sentYahoo = new YahooSent();
    YahooSpam spamYahoo = new YahooSpam();
    YahooTrash trashYahoo = new YahooTrash();
    
    FetchContentMail getContent = new FetchContentMail();
    FetchContentPlainText contentPlainText = new FetchContentPlainText();
    DeleteMail delete = new DeleteMail();
    SearchMail searchMail = new SearchMail();
    loadContentFailed contentFailed = new loadContentFailed();
    //</editor-fold>
    /**
     * Creates new form HomePage
     */
    public HomePage() {
        initComponents();      
        //icon mặc định của phần mềm
        IconImageUtilities.setIconImage(this);       
        //load danh sách email
        loadEmailList();
        //mặc định tạm tắt chức năng trừ thêm tài khoản, thông tin về phần mềm
        disableFunction();
        loadingMesseage.setVisible(false);
        searchStatus.setVisible(false);
        //////////////////////////////////////////////////////////
        //macOS ONLY
        main.desktopMac();
        //////////////////////////////////////////////////////////////////
        //Người dùng mới
        firstTime();
    }
    
    private void firstTime() { //người dùng mới
        Thread newuser = new Thread() {
            @Override
            public void run() {
                try {          
                    ps = connection.prepareStatement("SELECT COUNT(*) AS AMOUNT FROM email");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        int amount = Integer.parseInt(rs.getString("AMOUNT"));
                        if (amount == 0) {
                            int reply = JOptionPane.showConfirmDialog(null, "Xin chào người dùng mới!\n"
                            + "Có vẻ đây là lần đầu sử dụng của bạn, bạn có muốn thêm tài khoản không", "Thông báo", JOptionPane.YES_NO_OPTION);
                            if (reply == JOptionPane.YES_OPTION ) {
                                frameAddAccount.callframe();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        newuser.start();
        
    }
    
    private void enableFunction() {
        composeMail.setEnabled(true);
        inboxMail.setEnabled(true);    
        sentMail.setEnabled(true);
        spamMail.setEnabled(true);
        trashMail.setEnabled(true);
        deleteMail.setEnabled(true);
        replyMail.setEnabled(true);
        forwardMail.setEnabled(true);
        plainTextBtn.setEnabled(true);
        mailMessage.setEnabled(true);
    }
    
    private void disableFunction() {
        composeMail.setEnabled(false);
        inboxMail.setEnabled(false);
        sentMail.setEnabled(false);
        spamMail.setEnabled(false);
        trashMail.setEnabled(false);
        deleteMail.setEnabled(false);
        replyMail.setEnabled(false);
        forwardMail.setEnabled(false);
        plainTextBtn.setEnabled(false);
        mailMessage.setEnabled(false);
    }
    
    private void loadEmailList() {
        try {          
            ps = connection.prepareStatement("SELECT email FROM email");
            rs = ps.executeQuery();
            while (rs.next()) {
               String email = rs.getString("email");
               emailList.addItem(email);
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isEmpty() {
        if (mailList != null && mailList.getModel() != null) {
            return mailList.getModel().getRowCount()<=0;
        }
        return false;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        emailList = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        composeMail = new javax.swing.JButton();
        sentMail = new javax.swing.JButton();
        spamMail = new javax.swing.JButton();
        inboxMail = new javax.swing.JButton();
        trashMail = new javax.swing.JButton();
        refreshAccount = new javax.swing.JButton();
        username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mailList = new javax.swing.JTable();
        MailSearchInput = new javax.swing.JTextField();
        deleteMail = new javax.swing.JButton();
        replyMail = new javax.swing.JButton();
        forwardMail = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        fromTXT = new javax.swing.JTextField();
        dateTXT = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        subjectTXT = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        mailMessage = new javax.swing.JEditorPane();
        jLabel3 = new javax.swing.JLabel();
        folderMailName = new javax.swing.JLabel();
        loadingMesseage = new javax.swing.JLabel();
        plainTextBtn = new javax.swing.JButton();
        searchSelection = new javax.swing.JComboBox<>();
        searchStatus = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        CheckNetwork = new javax.swing.JMenuItem();
        accountManage = new javax.swing.JMenuItem();
        closeMenu = new javax.swing.JMenuItem();
        Help = new javax.swing.JMenu();
        aboutSoftware = new javax.swing.JMenuItem();
        aboutTeam = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Email Client Manager");
        setIconImage(getIconImage());

        emailList.setFont(new java.awt.Font("SF Pro Display", 0, 15)); // NOI18N
        emailList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn tài khoản" }));
        emailList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailListActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("SF Pro Display", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-email-64.png"))); // NOI18N

        composeMail.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        composeMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-writer-50.png"))); // NOI18N
        composeMail.setText(" Soạn thư");
        composeMail.setToolTipText("Soạn thư mới");
        composeMail.setBorderPainted(false);
        composeMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        composeMail.setMargin(new java.awt.Insets(2, -6, 2, 14));
        composeMail.setPreferredSize(new java.awt.Dimension(180, 70));
        composeMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                composeMailActionPerformed(evt);
            }
        });

        sentMail.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        sentMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-sent-50.png"))); // NOI18N
        sentMail.setText("   Đã gửi");
        sentMail.setToolTipText("Các thư đã gửi đi");
        sentMail.setBorderPainted(false);
        sentMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sentMail.setIconTextGap(-8);
        sentMail.setMargin(new java.awt.Insets(2, -2, 2, 14));
        sentMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sentMailActionPerformed(evt);
            }
        });

        spamMail.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        spamMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-spam-can-40.png"))); // NOI18N
        spamMail.setText("  Thư rác (spam)");
        spamMail.setToolTipText("Cẩn thận với thư lừa đảo");
        spamMail.setBorderPainted(false);
        spamMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        spamMail.setMargin(new java.awt.Insets(2, 0, 2, 14));
        spamMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spamMailActionPerformed(evt);
            }
        });

        inboxMail.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        inboxMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-inbox-50.png"))); // NOI18N
        inboxMail.setText("   Hộp thư đến");
        inboxMail.setToolTipText("Mở hộp thư đến");
        inboxMail.setBorderPainted(false);
        inboxMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        inboxMail.setIconTextGap(-8);
        inboxMail.setMargin(new java.awt.Insets(2, -4, 2, 14));
        inboxMail.setPreferredSize(new java.awt.Dimension(180, 70));
        inboxMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inboxMailActionPerformed(evt);
            }
        });

        trashMail.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        trashMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-trash-40.png"))); // NOI18N
        trashMail.setText(" Thùng rác");
        trashMail.setToolTipText("Các thư đã xóa lưu ở đây");
        trashMail.setBorderPainted(false);
        trashMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        trashMail.setMargin(new java.awt.Insets(2, 0, 2, 14));
        trashMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trashMailActionPerformed(evt);
            }
        });

        refreshAccount.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        refreshAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-refresh-30.png"))); // NOI18N
        refreshAccount.setText(" Làm mới");
        refreshAccount.setToolTipText("Làm mới danh sách tài khoản");
        refreshAccount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        refreshAccount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        refreshAccount.setMargin(new java.awt.Insets(2, 4, 2, 14));
        refreshAccount.setMaximumSize(new java.awt.Dimension(155, 59));
        refreshAccount.setMinimumSize(new java.awt.Dimension(155, 59));
        refreshAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAccountActionPerformed(evt);
            }
        });

        username.setFont(new java.awt.Font("SF Pro Display", 0, 18)); // NOI18N
        username.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        username.setText("Email Manger");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 38, Short.MAX_VALUE)
                .addComponent(emailList, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(refreshAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inboxMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(composeMail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sentMail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spamMail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(trashMail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(username, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(emailList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(refreshAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(composeMail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(inboxMail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(sentMail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(spamMail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(trashMail, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(220, Short.MAX_VALUE))
        );

        mailList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Người gửi", "Tiêu đề", "Ngày gửi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mailList.getTableHeader().setReorderingAllowed(false);
        mailList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mailListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(mailList);

        MailSearchInput.setFont(new java.awt.Font("SF Pro Display", 0, 14)); // NOI18N
        MailSearchInput.setToolTipText("Nhập thư cần tìm");
        MailSearchInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MailSearchInputKeyPressed(evt);
            }
        });

        deleteMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-delete-32.png"))); // NOI18N
        deleteMail.setText("Xóa thư");
        deleteMail.setToolTipText("Xóa thư");
        deleteMail.setBorderPainted(false);
        deleteMail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteMail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMailActionPerformed(evt);
            }
        });

        replyMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-reply-32.png"))); // NOI18N
        replyMail.setText("Trả lời thư");
        replyMail.setToolTipText("Trả lời thư");
        replyMail.setBorderPainted(false);
        replyMail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        replyMail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        replyMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replyMailActionPerformed(evt);
            }
        });

        forwardMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-forward-32.png"))); // NOI18N
        forwardMail.setText("Chuyển tiếp");
        forwardMail.setToolTipText("Chuyển tiếp thư");
        forwardMail.setBorderPainted(false);
        forwardMail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        forwardMail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        forwardMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardMailActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Từ");

        fromTXT.setFont(new java.awt.Font("SF Pro Display", 0, 14)); // NOI18N
        fromTXT.setToolTipText("Người gửi");

        dateTXT.setFont(new java.awt.Font("SF Pro Display", 0, 14)); // NOI18N
        dateTXT.setToolTipText("Ngày gửi thư");

        jLabel4.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Ngày gửi");

        jLabel5.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Tiêu đề");

        subjectTXT.setFont(new java.awt.Font("SF Pro Display", 0, 14)); // NOI18N
        subjectTXT.setToolTipText("Tiêu đề thư");

        jLabel6.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Nội dung");

        jLabel8.setFont(new java.awt.Font("SF Pro Display", 0, 16)); // NOI18N
        jLabel8.setText("Nhập để tìm kiếm thư");

        mailMessage.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jScrollPane3.setViewportView(mailMessage);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 15)); // NOI18N
        jLabel3.setText("Danh sách thư:");

        folderMailName.setFont(new java.awt.Font("Dialog", 0, 15)); // NOI18N
        folderMailName.setText("...............................");

        loadingMesseage.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        loadingMesseage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loadingMesseage.setText("Đang tải nội dung ...");

        plainTextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-multiline-text-32.png"))); // NOI18N
        plainTextBtn.setText("Plain Text");
        plainTextBtn.setBorderPainted(false);
        plainTextBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        plainTextBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        plainTextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plainTextBtnActionPerformed(evt);
            }
        });

        searchSelection.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        searchSelection.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Theo người gửi", "Theo ngày gửi" }));

        searchStatus.setFont(new java.awt.Font("Dialog", 0, 15)); // NOI18N
        searchStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        searchStatus.setText(".....................");
        searchStatus.setToolTipText("");

        jMenu1.setText("File");

        CheckNetwork.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-internet-30.png"))); // NOI18N
        CheckNetwork.setText("Kiểm tra kết nối");
        CheckNetwork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckNetworkActionPerformed(evt);
            }
        });
        jMenu1.add(CheckNetwork);

        accountManage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-users-settings-30.png"))); // NOI18N
        accountManage.setText("Quản lý tài khoản");
        accountManage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountManageActionPerformed(evt);
            }
        });
        jMenu1.add(accountManage);

        closeMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        closeMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-exit-30.png"))); // NOI18N
        closeMenu.setText("Thoát");
        closeMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuActionPerformed(evt);
            }
        });
        jMenu1.add(closeMenu);

        jMenuBar1.add(jMenu1);

        Help.setText("About");

        aboutSoftware.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-software-30.png"))); // NOI18N
        aboutSoftware.setText("Về phần mềm");
        aboutSoftware.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutSoftwareActionPerformed(evt);
            }
        });
        Help.add(aboutSoftware);

        aboutTeam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8-team-30.png"))); // NOI18N
        aboutTeam.setText("Về nhóm");
        aboutTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutTeamActionPerformed(evt);
            }
        });
        Help.add(aboutTeam);

        jMenuBar1.add(Help);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(folderMailName, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(359, 359, 359))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(MailSearchInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fromTXT))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dateTXT))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(subjectTXT))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(deleteMail, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(replyMail, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(forwardMail, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(plainTextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 237, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
                        .addComponent(loadingMesseage, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteMail, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(replyMail, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(forwardMail, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(plainTextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(fromTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(dateTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(subjectTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(loadingMesseage))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(MailSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchStatus)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(folderMailName)))
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CheckNetworkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckNetworkActionPerformed
        // TODO add your handling code here:
        //kiểm tra kết nối Internet
        NetworkNotify.NotifyMesseage();
        
    }//GEN-LAST:event_CheckNetworkActionPerformed

    private void closeMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeMenuActionPerformed
        // TODO add your handling code here:
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_closeMenuActionPerformed

    private void accountManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountManageActionPerformed
        // TODO add your handling code here:
        frameManageAccount.callframe();
    }//GEN-LAST:event_accountManageActionPerformed

   
    private void inboxMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inboxMailActionPerformed
        // TODO add your handling code here:      
        if (isEmpty() == false) { 
            int reply = JOptionPane.showConfirmDialog(this, "Bảng đã có danh sách thư, bạn muốn tiếp tục?\n"
                    + "Nếu bạn muốn tải thư mới, bạn có thể tiếp tục.", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION ) {
                inboxMailAction();
            }         
        }
        else {
            inboxMailAction();
        }     
    }//GEN-LAST:event_inboxMailActionPerformed

    private void composeMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_composeMailActionPerformed
        // TODO add your handling code here:
        //gọi frame soạn thư. Hết 
        user = emailList.getSelectedItem().toString();
        sendGUI.callSendframe(user);
    }//GEN-LAST:event_composeMailActionPerformed
     
    //Thư đã gửi
    private void sentMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sentMailActionPerformed
        if (isEmpty() == false) { 
        int reply = JOptionPane.showConfirmDialog(this, "Bảng đã có danh sách thư, bạn muốn tiếp tục?\n"
                + "Nếu bạn muốn tải thư mới, bạn có thể tiếp tục.", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION ) {
            sentMailAction();
            }         
        }
        else {
            sentMailAction();
        }     
        
    }//GEN-LAST:event_sentMailActionPerformed

    private void spamMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spamMailActionPerformed
        // TODO add your handling code here:        
        if (isEmpty() == false) { 
            int reply = JOptionPane.showConfirmDialog(this, "Bảng đã có danh sách thư, bạn muốn tiếp tục?\n"
                    + "Nếu bạn muốn tải thư mới, bạn có thể tiếp tục.", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION ) {
                spamMailAction();
            }         
        }
        else {
            spamMailAction();
        }     
    }//GEN-LAST:event_spamMailActionPerformed

    private void trashMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trashMailActionPerformed
        // TODO add your handling code here: 
        if (isEmpty() == false) { 
            int reply = JOptionPane.showConfirmDialog(this, "Bảng đã có danh sách thư, bạn muốn tiếp tục?\n"
                    + "Nếu bạn muốn tải thư mới, bạn có thể tiếp tục.", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION ) {
                trashMailAction();
            }         
        }
        else {
            trashMailAction();
        }     
    }//GEN-LAST:event_trashMailActionPerformed
  
    
    private void mailListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mailListMouseClicked
        // TODO add your handling code here:
        //đọc nội dung thư       
        DefaultTableModel getmodel = (DefaultTableModel) mailList.getModel();
        fromTXT.setText(getmodel.getValueAt(mailList.getSelectedRow(),0).toString());
        dateTXT.setText(getmodel.getValueAt(mailList.getSelectedRow(),2).toString());
        try {
            subjectTXT.setText(getmodel.getValueAt(mailList.getSelectedRow(),1).toString());
        } catch (NullPointerException ex) {
            subjectTXT.setText("Mail không có tiêu đề");
        }
        String getSearchStatus = searchStatus.getText();
        if (getSearchStatus.equals("Hoàn thành...")) {
            loadSearchContent(mailList.getSelectedRow());
        }
        else {
            loadContent(mailList.getSelectedRow());
        }
           
    }//GEN-LAST:event_mailListMouseClicked

    private void refreshAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAccountActionPerformed
        // TODO add your handling code here:
        String firstItem = "Chọn tài khoản";        
        DefaultComboBoxModel getmodel = new DefaultComboBoxModel();
        getmodel.addElement(firstItem);
        emailList.setModel(getmodel);      
        Thread loadeEmail = new Thread() {
            @Override
            public void run() {             
                loadEmailList();
            }
        };
        loadeEmail.start();
        
    }//GEN-LAST:event_refreshAccountActionPerformed

    private void emailListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailListActionPerformed
        // TODO add your handling code here:
        String stringSelected = emailList.getSelectedItem().toString();
        switch (stringSelected) {
            case "Chọn tài khoản" -> {
                //nếu danh sách emailList ở chọn tài khoản
                //vô hiệu hóa tất cả chức năng tại màn hình chính
                disableFunction();
                username.setText("Email Manager");
            }
            default -> {
                enableFunction();
                try {
                    //trước tiên chúng ta sẽ đổi Email Client thành tên của tk email
                    ps = connection.prepareStatement("SELECT name FROM email WHERE email = ?");
                    ps.setString(1, emailList.getSelectedItem().toString());
                    rs = ps.executeQuery();

                    while (rs.next()) {
                            username.setText(rs.getString("name"));
                            }

                } catch (SQLException ex) {
                        Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        }    
    }//GEN-LAST:event_emailListActionPerformed

    private void aboutSoftwareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutSoftwareActionPerformed
        // TODO add your handling code here:
        frameAboutSoftware.callframe();
    }//GEN-LAST:event_aboutSoftwareActionPerformed

    private void aboutTeamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutTeamActionPerformed
        // TODO add your handling code here:
        //frameAboutTeam.callframe();
        frameAboutTeam.anotherframe();
    }//GEN-LAST:event_aboutTeamActionPerformed

    private void plainTextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plainTextBtnActionPerformed
        // TODO add your handling code here:
        int countRow = mailList.getRowCount();
        if (countRow == 0) {
            JOptionPane.showMessageDialog(this, "Danh sách thư trống", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
        else if (mailList.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Chưa có thư nào được chọn!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
        else
            {
               showPlainText(mailList.getSelectedRow()); 
            }
               
    }//GEN-LAST:event_plainTextBtnActionPerformed

    private void forwardMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardMailActionPerformed
        // TODO add your handling code here:
        user = emailList.getSelectedItem().toString();
        fromUser = fromTXT.getText();
        date = dateTXT.getText();
        subject = subjectTXT.getText();
        mailContent = mailMessage.getText();
        foldermailname = folderMailName.getText();
        rowSelected = mailList.getSelectedRow();
        sendGUI.callForwardframe(user, fromUser, date, subject, mailContent, foldermailname, rowSelected);
        
    }//GEN-LAST:event_forwardMailActionPerformed

    private void replyMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replyMailActionPerformed
        // TODO add your handling code here:
        user = emailList.getSelectedItem().toString();
        fromUser = fromTXT.getText();
        date = dateTXT.getText();
        subject = subjectTXT.getText();
        mailContent = mailMessage.getText();
        foldermailname = folderMailName.getText();
        rowSelected = mailList.getSelectedRow();
        sendGUI.callReplyframe(user, fromUser, date, subject, mailContent, foldermailname, rowSelected);
        
    }//GEN-LAST:event_replyMailActionPerformed

    private void deleteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMailActionPerformed
        // TODO add your handling code here:
        Thread contentMail = new Thread() {
            @Override
            public void run() {
                try {
                ps = connection.prepareStatement("SELECT email, password, imap, type FROM email WHERE email = ?");
                ps.setString(1, emailList.getSelectedItem().toString());
                rs = ps.executeQuery();

                imap = rs.getString("imap");
                storeType = folderMailName.getText();
                user = rs.getString("email");
                password = rs.getString("password");
                rowSelected = mailList.getSelectedRow();
                delete.deleteEmail(rowSelected, imap, storeType, user, password, mailMessage);
                loadingMesseage.setVisible(false);
                } catch (SQLException | MessagingException | IOException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);                 
                }
            }
        };
                contentMail.start();
    }//GEN-LAST:event_deleteMailActionPerformed

    private void MailSearchInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MailSearchInputKeyPressed
        // TODO add your handling code here:
        String typeSearch = searchSelection.getSelectedItem().toString();
        mailsearchInput = MailSearchInput.getText();
        foldermailname = folderMailName.getText();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchStatus.setVisible(true);
            searchStatus.setText("Đang tìm kiếm...");          
            searchMailStart(mailsearchInput, typeSearch, foldermailname);
        }
    }//GEN-LAST:event_MailSearchInputKeyPressed
   
    private void searchMailStart(String mailsearchInput, String typeSearch, String mailfolder) {
        mailList.setEnabled(false);
        Thread startSearch = new Thread() {
            @Override
            public void run() {
                try {
                    ps = connection.prepareStatement("SELECT email, password, imap, type FROM email WHERE email = ?");
                    ps.setString(1, emailList.getSelectedItem().toString());
                    rs = ps.executeQuery();

                    imap = rs.getString("imap");
                    storeType = rs.getString("type");
                    user = rs.getString("email");
                    password = rs.getString("password"); 
                    
                    mailList.setEnabled(true);
                    model = searchMail.startFetch(imap, storeType, user, password, mailsearchInput, typeSearch, mailfolder);
                    mailList.setModel(model);
                    model.setRowCount(0);                  
                    searchStatus.setText("Hoàn thành...");
                    
                } catch (SQLException | MessagingException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                    mailList.setEnabled(true);
                    searchStatus.setText("Hoàn thành...");
                }
            }
        };
        startSearch.start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //tất cả các quá trình fetch đều dùng thread
    //load nội dung thư 
    private void loadSearchContent(int rowSelected) {
        mailMessage.setText("");
        disableFunction();
        mailList.setEnabled(false);
        loadingMesseage.setVisible(true);
        loadingMesseage.setText("Đang tải nội dung ...");
        Thread contentSearch = new Thread() {
            @Override
            public void run() {
                try {
                    ps = connection.prepareStatement("SELECT email, password, imap, type FROM email WHERE email = ?");
                    ps.setString(1, emailList.getSelectedItem().toString());
                    rs = ps.executeQuery();

                    imap = rs.getString("imap");
                    storeType = rs.getString("type");
                    user = rs.getString("email");
                    password = rs.getString("password"); 
                    String typeSearch = searchSelection.getSelectedItem().toString();
                    mailsearchInput = MailSearchInput.getText();
                    foldermailname = folderMailName.getText();
                    
                    mailList.setEnabled(true);
                    searchMail.fetchResultContent(imap, storeType, user, password, 
                            mailsearchInput, typeSearch, foldermailname, rowSelected, mailMessage);
                    mailList.setModel(model);
                    model.setRowCount(0);                  
                    loadingMesseage.setVisible(false);
                    enableFunction();                  
                    mailList.setEnabled(true);
                } catch (SQLException | MessagingException | IOException  ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                    enableFunction();
                    mailList.setEnabled(true);
                }
                catch (ClassCastException ex) {
                    enableFunction();
                    mailList.setEnabled(true);
                    if(contentFailed.callNotify(rowSelected) == true) {
                        showPlainText(rowSelected);
                    }
                    else {
                        loadingMesseage.setText("");
                    }
                } 
            }
        };
        contentSearch.start();
    }
    private void loadContent(int rowSelected) {
        mailMessage.setText("");
        disableFunction();
        mailList.setEnabled(false);
        loadingMesseage.setVisible(true);
        loadingMesseage.setText("Đang tải nội dung ...");
        Thread contentMail = new Thread() {
            @Override
            public void run() {
                try {
                ps = connection.prepareStatement("SELECT email, password, imap, type FROM email WHERE email = ?");
                ps.setString(1, emailList.getSelectedItem().toString());
                rs = ps.executeQuery();

                imap = rs.getString("imap");
                storeType = folderMailName.getText();
                user = rs.getString("email");
                password = rs.getString("password");

                getContent.readEmail(rowSelected, imap, storeType, user, password, mailMessage);
                loadingMesseage.setVisible(false);
                mailList.setEnabled(true);
                enableFunction();
                } catch (SQLException | MessagingException | IOException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                    mailList.setEnabled(true);
                    enableFunction();                   
                }
                catch (ClassCastException ex) {
                    enableFunction();
                    mailList.setEnabled(true);
                    if(contentFailed.callNotify(rowSelected) == true) {
                        showPlainText(rowSelected);
                    }
                    else {
                        loadingMesseage.setText("");
                    }
                }
            }
        };
        contentMail.start();
    }
    
    public void showPlainText(int rowSelected) {
        mailMessage.setText("");
        disableFunction();
        mailList.setEnabled(false);
        loadingMesseage.setVisible(true);
        loadingMesseage.setText("Đang tải dạng plain text ...");
        Thread plaintextMail = new Thread() {
            @Override
            public void run() {
                try {
                ps = connection.prepareStatement("SELECT email, password, imap, type FROM email WHERE email = ?");
                ps.setString(1, emailList.getSelectedItem().toString());
                rs = ps.executeQuery();

                imap = rs.getString("imap");
                storeType = folderMailName.getText();
                user = rs.getString("email");
                password = rs.getString("password");                
                mailList.setEnabled(true);
                enableFunction();
                contentPlainText.plainTextShow(rowSelected, imap, storeType, user, password, mailMessage);
                loadingMesseage.setVisible(false);
                } catch (SQLException | MessagingException | IOException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        plaintextMail.start();
    }
    
    //với các hộp thư đến
    private void inboxMailAction()  {
        mailList.setModel(new DefaultTableModel(null, new String[]{"Người Gửi", "Tiêu Đề",  "Thời Gian" })); //xóa sạch thông tin bảng       
        disableFunction();
        downloadDialog.setModalityType(Dialog.ModalityType.MODELESS);
        downloadDialog.setVisible(true);
        //trick cho đọc nội dung mail
        folderMailName.setText(foldername.getInboxFolder());
        Thread inbox = new Thread() {
               @Override
                public void run() {                
                        try {
                        ps = connection.prepareStatement("SELECT email, password, imap, type FROM email WHERE email = ?");
                        ps.setString(1, emailList.getSelectedItem().toString());
                        rs = ps.executeQuery();

                        imap = rs.getString("imap");
                        storeType = rs.getString("type");
                        user = rs.getString("email");
                        password = rs.getString("password");                      
                        model = fetchInbox.startFetch(imap, storeType, user, password);
                        mailList.setModel(model);
                        model.setRowCount(0);
                        downloadDialog.setVisible(false);
                        enableFunction();
                        } catch (SQLException | MessagingException ex) {
                            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                            downloadDialog.setVisible(false);
                            enableFunction();
                        }
                    }
                };
                inbox.start();
    }    
    //với các hộp thư đã gửi
    private void sentMailAction() {
        mailList.setModel(new DefaultTableModel(null, new String[]{"Người Gửi", "Tiêu Đề",  "Thời Gian" })); //xóa sạch thông tin bảng       
        disableFunction();
        downloadDialog.setModalityType(Dialog.ModalityType.MODELESS);
        downloadDialog.setVisible(true);
        Thread sent = new Thread() {
               @Override
                public void run() {                
                        try {
                        ps = connection.prepareStatement("SELECT email, password, imap, type, server FROM email WHERE email = ?");
                        ps.setString(1, emailList.getSelectedItem().toString());
                        rs = ps.executeQuery();

                       imap = rs.getString("imap");
                       storeType = rs.getString("type");
                       user = rs.getString("email");
                       password = rs.getString("password");

                       server = rs.getString("server");
                        switch (server) {
                            case "gmail" -> {
                                    model = sentGmail.startFetch(imap, storeType, user, password);
                                    mailList.setModel(model);
                                    model.setRowCount(0);
                                    folderMailName.setText(foldername.getSentGmail());
                                    break;
                            }
                            case "outlook" -> {
                                    model = sentOutlook.startFetch(imap, storeType, user, password);
                                    mailList.setModel(model);
                                    model.setRowCount(0);
                                    folderMailName.setText(foldername.getSentOutlook());
                                    break;
                            }
                            case "yahoo" -> {
                                    model = sentYahoo.startFetch(imap, storeType, user, password);
                                    mailList.setModel(model);
                                    model.setRowCount(0);
                                    folderMailName.setText(foldername.getSentYahoo());
                                    break;
                            }
                            default -> {
                                    break;
                            }
                        }                
                        downloadDialog.setVisible(false);
                        enableFunction();
                        } catch (SQLException | MessagingException ex) {
                            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                            downloadDialog.setVisible(false);
                            enableFunction();
                        }
                    }
                };
                sent.start();
    }
    //với các hộp thư rác spam
    private void spamMailAction() {
        mailList.setModel(new DefaultTableModel(null, new String[]{"Người Gửi", "Tiêu Đề",  "Thời Gian" })); //xóa sạch thông tin bảng
        disableFunction();
        downloadDialog.setModalityType(Dialog.ModalityType.MODELESS);
        downloadDialog.setVisible(true);
        Thread spam = new Thread() {
               @Override
                public void run() {                
                        try {
                        ps = connection.prepareStatement("SELECT email, password, imap, type, server FROM email WHERE email = ?");
                        ps.setString(1, emailList.getSelectedItem().toString());
                        rs = ps.executeQuery();

                       imap = rs.getString("imap");
                       storeType = rs.getString("type");
                       user = rs.getString("email");
                       password = rs.getString("password");

                       server = rs.getString("server");
                        switch (server) {
                            case "gmail" -> {
                                    mailList.setModel(spamGmail.startFetch(imap, storeType, user, password));
                                    folderMailName.setText(foldername.getSpamGmail());
                                    break;
                            }
                            case "outlook" -> {
                                    mailList.setModel(spamOutlook.startFetch(imap, storeType, user, password));
                                    folderMailName.setText(foldername.getSpamOutlook());
                                    break;
                            }
                            case "yahoo" -> {
                                    spamYahoo.notifyMesseage();
                                    break;
                            }
                            default -> {
                                    break;
                            }
                        }                
                        downloadDialog.setVisible(false);
                        enableFunction();
                        } catch (SQLException | MessagingException ex) {
                            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                            downloadDialog.setVisible(false);
                            enableFunction();
                        }
                    }
                };
                spam.start();
        
    }
    //với các thùng rác
    private void trashMailAction() {
        mailList.setModel(new DefaultTableModel(null, new String[]{"Người Gửi", "Tiêu Đề",  "Thời Gian" })); //xóa sạch thông tin bảng
        disableFunction();
        downloadDialog.setModalityType(Dialog.ModalityType.MODELESS);
        downloadDialog.setVisible(true);
        Thread spam = new Thread() {
               @Override
                public void run() {                
                        try {
                        ps = connection.prepareStatement("SELECT email, password, imap, type, server FROM email WHERE email = ?");
                        ps.setString(1, emailList.getSelectedItem().toString());
                        rs = ps.executeQuery();

                       imap = rs.getString("imap");
                       storeType = rs.getString("type");
                       user = rs.getString("email");
                       password = rs.getString("password");

                       server = rs.getString("server");
                        switch (server) {
                            case "gmail" -> {
                                    mailList.setModel(trashGmail.startFetch(imap, storeType, user, password));
                                    folderMailName.setText(foldername.getTrashGmail());
                                    break;
                            }
                            case "outlook" -> {
                                    mailList.setModel(trashOutlook.startFetch(imap, storeType, user, password));
                                    folderMailName.setText(foldername.getTrashOutlook());
                                    break;
                            }
                            case "yahoo" -> {
                                    mailList.setModel(trashYahoo.startFetch(imap, storeType, user, password));
                                    folderMailName.setText(foldername.getTrashYahoo());
                                    break;
                            }
                            default -> {
                                    break;
                            }
                        }                
                        downloadDialog.dispose();
                        enableFunction();
                        } catch (SQLException | MessagingException ex) {
                            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                            downloadDialog.setVisible(false);
                            enableFunction();
                        }
                    }
                };
                spam.start();
        
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
    public static void main() {
        /* Set the FlaLaf Light */
        //theme này đẹp hơn nhiều
        LookandFeel.setTheme();
        mainForm.isMacOS();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            HomePage frame = new HomePage();
            frame.setVisible(true);
            mainForm.isMacFullWindowContentSupported(frame);           
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CheckNetwork;
    private javax.swing.JMenu Help;
    private javax.swing.JTextField MailSearchInput;
    private javax.swing.JMenuItem aboutSoftware;
    private javax.swing.JMenuItem aboutTeam;
    private javax.swing.JMenuItem accountManage;
    private javax.swing.JMenuItem closeMenu;
    private javax.swing.JButton composeMail;
    private javax.swing.JTextField dateTXT;
    private javax.swing.JButton deleteMail;
    private javax.swing.JComboBox<String> emailList;
    private javax.swing.JLabel folderMailName;
    private javax.swing.JButton forwardMail;
    private javax.swing.JTextField fromTXT;
    private javax.swing.JButton inboxMail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    public javax.swing.JLabel loadingMesseage;
    public javax.swing.JTable mailList;
    private javax.swing.JEditorPane mailMessage;
    private javax.swing.JButton plainTextBtn;
    private javax.swing.JButton refreshAccount;
    private javax.swing.JButton replyMail;
    private javax.swing.JComboBox<String> searchSelection;
    private javax.swing.JLabel searchStatus;
    private javax.swing.JButton sentMail;
    private javax.swing.JButton spamMail;
    private javax.swing.JTextField subjectTXT;
    private javax.swing.JButton trashMail;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
