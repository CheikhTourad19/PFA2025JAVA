package pfa.java.pfa2025java;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Replace with your Twilio account SID, auth token, and Twilio phone number
    public static final String ACCOUNT_SID = "ACdbe0fe323dbc301d92992eea69b19aa0";
    public static final String AUTH_TOKEN = "638b3395b09a4eb95934bd766891cde3";
    public static final String FROM_PHONE_NUMBER = "+15173144753";

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

