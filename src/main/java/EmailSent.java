import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class EmailSent {
    public static void main(String[] args) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(EmailSent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);

        message.setFrom(new InternetAddress(properties.getProperty("mail.smtps.emailFrom")));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(properties.getProperty("mail.smtps.emailTo")));
        message.setSubject("test subject");
        message.setText("Hi this is my test message 2");

        Transport tr = mailSession.getTransport();
        tr.connect(null, properties.getProperty("mail.smtps.emailFromPass"));
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}