import javax.mail.*;
import javax.mail.internet.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class InviteSent {
    private static SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
    private static String sentTo;
    private static String sentFrom;
    private static String subject;
    private static String mailBody;
    private static String uid;

    InviteSent(String sendTo, String subject, String mailBody, Date startDate, Date endDate, String uid ) throws IOException, MessagingException {
        this.sentTo = sendTo;
        this.subject = subject;
        this.mailBody = mailBody;
        this.uid = uid;
        sent(startDate, endDate);
    }

    private void sent(Date startDate, Date endDate) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(InviteSent.class.getClassLoader().getResourceAsStream("mail.properties"));
        sentTo =properties.getProperty("mail.smtps.emailTo");
        sentFrom= properties.getProperty("mail.smtps.emailFrom");

        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(sentFrom));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sentTo));
        message.setSubject(subject);

        //StringBuffer sb = new StringBuffer();
        System.out.println(mailBody);
        String calendarContent = "BEGIN:VCALENDAR\n" +
                "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" +
                "VERSION:2.0\n" +
                "METHOD:REQUEST\n" +
                "BEGIN:VEVENT\n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+sentTo+"\n" +
                "ORGANIZER:MAILTO:"+sentFrom+"\n" +
                "DTSTAMP:" + iCalendarDateFormat.format(startDate) + "\n" +
                "DTSTART:" + iCalendarDateFormat.format(startDate) + "\n" +
                "DTEND:" + iCalendarDateFormat.format(endDate) + "\n" +
                "UID:"+uid+"\n" +
                "LOCATION:+TH-3\n" +
                "TRANSP:OPAQUE\n" +
                "SEQUENCE:0\n" +
                "CATEGORIES:Session\n" +
                "DESCRIPTION:\n\n" +
                "SUMMARY:"+subject+"\n" +
                "PRIORITY:5\n" +
                "CLASS:PUBLIC\n" +
                "BEGIN:VALARM\n" +
                "TRIGGER:PT1440M\n" +
                "ACTION:DISPLAY\n" +
                "DESCRIPTION:Reminder\n" +
                "END:VALARM\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

        // Create the message part
        BodyPart messageCalendar = new MimeBodyPart();

        // Fill the message
        messageCalendar.setHeader("Content-Class", "urn:content-classes:calendarmessage");
        //messageCalendar.setHeader("Content-ID", "calendar_message");
        //messageCalendar.setContent(mailBody, "BAKCHODI");
        // messageCalendar.setDataHandler(new DataHandler(                new ByteArrayDataSource(buffer.toString(), "text/calendar")));
        messageCalendar.setContent(calendarContent,"text/calendar");

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