# Email_client
- Đây là project bài tập kết thúc môn Lập trình mạng
- Báo cáo word và powerpoint lưu vào thư mục documents
- IDE: Apache Netbeans (mới nhất) chỉ cần ít nhất trong phiên bản 12 cũng được
- JDK: khuyến cáo: JDK 17 trở lên do source đã đẩy lên JDK 17 LTS
- Vui lòng để lại comment trong dòng code để các thành viên hiểu function của code
- Muốn hiểu các Folder Email sử dụng trong bài, truy cập link dưới và chọn package CheckEmailFolder để có thêm thông tin
- https://github.com/2dgirlismywaifu/Network_development_Java
# Mục tiêu
- Chú ý: các mục tiêu các thành viên có thể add thêm
1. Có đầy đủ chức năng gửi thư và fetch đc nội dung hộp thư đến của tài khoản
2. Có thể thêm nhiều tài khoản
3. Kiểm tra định dạnh email hợp lệ
4. Có thể đính kèm tập tin để gửi
5. Với email có tính năng xác thực hai bước vui lòng sử dụng mật khẩu ứng dụng
6. Có tính năng đề xuất gì mới thì thêm vào
# Look and Feel (Hay gọi là giao diện cũng được)
- Giao diện: Flatlaf Light
- Class dùng chung: lookandfeel.java (folder global)
- Cách goi: lookandfeel.setTheme();
- Thêm vào sau: public static void main(String args[]) { .... }
- Trước khi thêm vui lòng xóa: Look and feel settings code (optional) trong public static void main(String args[]) { .... }
# Các thư viện hỗ trợ
1. activation.jar
2. javax.mail-1.6.2.jar
3. sqlite-jdbc-3.36.0.3.jar
4. flatlaf-2.3.jar (chính là giao diện đó)
# Thông tin và các nguồn github tham khảo
1. Tech-geek29: https://github.com/tech-geek29/java-email-client
2. RawSanji: https://github.com/RawSanj/java-mail-clients
3. Đấng sharecode (10k rẻ vl): https://gvbax-my.sharepoint.com/:u:/g/personal/user1839_offices365_co/EbfGwRhacgBDmdrzwYBIvBsBecSA99ZYLoV8RxNC75dwmQ?e=ZLR7Cb
# Xin chú ý: các thành viên cần để ý
1. Anti-virus có thể ngăn kết nối SSL HandShake. Nếu anti-virus quét các gói tin SSL, vui lòng vô hiệu hoá.
2. Tài khoản có xác thực hai bước cần sử dụng mật khẩu ứng dụng
3. Fetch nhanh hay chậm tuỳ thuộc Internet nhanh hay chậm
4. Không còn hỗ trợ pop3. Sử dụng IMAP thay thế
5. Một số mail không thể load được nội dụng. Đây là vấn đề thuộc JavaMail và chúng ta không thể làm được gì cả.
6. Do thay đổi về security các dịch vụ Email (Gmail, Outlook, Yahoo...), tất cả các tài khoản thêm vào nên sử dụng xác thực hai bước và tạo mật khẩu ứng dụng.
# macOS Support
- Các dòng properties hỗ trợ cho macOS
1. System.setProperty( "apple.laf.useScreenMenuBar", "true" ); //menubar lên Screen Menu Bar
2. System.setProperty( "apple.awt.application.name", "FlatLaf Demo" ); //tên phần mềm lên Screen Menu Bar
3. System.setProperty( "apple.awt.application.appearance", "system" ); //titlebar theo system của macos
4. if( SystemInfo.isMacFullWindowContentSupported )
    frame.getRootPane().putClientProperty( "apple.awt.transparentTitleBar", true ); //transparent titlebar
5. frame.getRootPane().putClientProperty( "apple.awt.windowTitleVisible", false ); //ẩn tên phần mềm trên titlebar
6. frame.getRootPane().putClientProperty( "apple.awt.fullscreenable", true ); //fullscreen mode
7. Thông tin thêm tại: https://www.formdev.com/flatlaf/macos/#window_settings
# Giai đoạn cuối. Kiểm thử phần mềm
- Source đã hoàn thành
- Tiến hành việc kiểm thử