import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.Properties;

public class InviteSent {
    public static void main(String[] args) throws IOException, MessagingException {
        String mailBody = "Some body text";

        final Properties properties = new Properties();
        properties.load(InviteSent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(properties.getProperty("mail.smtps.emailFrom")));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(properties.getProperty("mail.smtps.emailTo")));
        message.setSubject("test subject");
        //message.setText("Hi this is my test message 2");

        //--------------------------
        StringBuffer sb = new StringBuffer();
        System.out.println(mailBody);
        StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" +
                "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" +
                "VERSION:2.0\n" +
                "METHOD:REQUEST\n" +
                "BEGIN:VEVENT\n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+properties.getProperty("mail.smtps.emailTo")+"\n" +
                "ORGANIZER:MAILTO:"+properties.getProperty("mail.smtps.emailFrom")+"\n" +
                "DTSTART:20180404T053000Z\n" +
                "DTEND:20180404T060000Z\n" +
                "LOCATION:+TH-1\n" +
                "TRANSP:OPAQUE\n" +
                "SEQUENCE:0\n" +
                "UID:040000008200E00074C5B7101A82E00800000000002FF466CE3AC5010000000000000000100\n" +
                " 000004377FE5C37984842BF9440448399EB02\n" +
                "DTSTAMP:20051206T120102Z\n" +
                "CATEGORIES:Session\n" +
                "DESCRIPTION:\n\n" +
                "SUMMARY:Session for tomorrow\n" +
                "PRIORITY:5\n" +
                "CLASS:PUBLIC\n" +
                "BEGIN:VALARM\n" +
                "TRIGGER:PT1440M\n" +
                "ACTION:DISPLAY\n" +
                "DESCRIPTION:Reminder\n" +
                "END:VALARM\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR");

        // Create the message part
        BodyPart messageCalendar = new MimeBodyPart();

        // Fill the message
        messageCalendar.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
        messageCalendar.setHeader("Content-ID", "calendar_message");
        messageCalendar.setContent(mailBody, "BAKCHODI");
        messageCalendar.setDataHandler(new DataHandler(
                new ByteArrayDataSource(buffer.toString(), "text/calendar")));

        MimeBodyPart bc = new MimeBodyPart();
        bc.setContent(mailBody,"text/html");

        BodyPart messageBody = bc;
        Multipart multipart = new MimeMultipart();

        // Add part one

        multipart.addBodyPart(messageBody);
        multipart.addBodyPart(messageCalendar);

        //Put parts in message
        message.setContent(multipart);
        //--------------------------

        Transport tr = session.getTransport();
        tr.connect(null, properties.getProperty("mail.smtps.emailFromPass"));
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}