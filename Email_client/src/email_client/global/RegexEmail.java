package email_client.global;

import java.util.regex.Pattern;

public class RegexEmail {
    public static boolean validation (String email) {
        String email_Pattern = "^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(email_Pattern);
        return pattern.matcher(email).matches();
    }   
}
