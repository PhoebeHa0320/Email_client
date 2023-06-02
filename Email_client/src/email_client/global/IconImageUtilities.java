package email_client.global;

import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class IconImageUtilities {
    public static void setIconImage(Window window)
    {
        try
        {
            InputStream imageInputStream = window.getClass().getResourceAsStream("/icon/icons8-email-64.png");
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            window.setIconImage(bufferedImage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
