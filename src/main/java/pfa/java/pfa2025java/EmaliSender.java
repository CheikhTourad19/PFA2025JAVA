package pfa.java.pfa2025java;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmaliSender {

    public static boolean sendEmail(String toEmail, String message1) {
        final String username = "donotreplyemedical@gmail.com"; // Your email address
        final String appPassword = "qmmc zhsl mfeh jfrs"; // Your email app password

        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set the sender and recipient addresses
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            // Set the email subject and body
            message.setSubject("Email de E-Medical ");
            message.setText(message1);

            // Send the email
            Transport.send(message);
            return true; // Email sent successfully

        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Email failed to send
        }
    }
}
