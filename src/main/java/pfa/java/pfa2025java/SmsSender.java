package pfa.java.pfa2025java;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;

public class SmsSender {
    private static final Dotenv dotenv = Dotenv.load();

    // Replace with your Twilio account SID, auth token, and Twilio phone number
    public static final String ACCOUNT_SID = dotenv.get("SMS_SID");
    public static final String AUTH_TOKEN = dotenv.get("SMS_AUTH_TOKEN");
    public static final String FROM_PHONE_NUMBER = dotenv.get("SMS_NUMBER");

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static boolean sendSms(String toPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber("+216"+toPhoneNumber),
                    new PhoneNumber(FROM_PHONE_NUMBER),
                    messageBody
            ).create();

            System.out.println("Message sent: " + message.getSid());
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            return false;
        }
    }
}

