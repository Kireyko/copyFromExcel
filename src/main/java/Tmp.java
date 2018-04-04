import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Tmp {
    public static void main(String[] args) throws IOException, MessagingException {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 1);
        Date end = cal.getTime();
        System.out.println(start);
        System.out.println(end);

        final Properties properties = new Properties();
        properties.load(InviteSent.class.getClassLoader().getResourceAsStream("mail.properties"));

        InviteSent inviteSent = new InviteSent(properties.getProperty("mail.smtps.emailTo"), "mail subj", "body",start,end , "12345");

    }
}
