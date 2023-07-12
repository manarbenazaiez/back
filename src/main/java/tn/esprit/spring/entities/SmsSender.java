package tn.esprit.spring.entities;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Twilio account credentials
    private static final String ACCOUNT_SID = "AC9ad44165e118d834ee98ee4fd04b612d";
    private static final String AUTH_TOKEN = "e14dff68a5f124dceac494068247840a";

    public static void sendSms(String recipientPhoneNumber, String message) {
        // Set up Twilio client
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Send the SMS
        Message.creator(new PhoneNumber(recipientPhoneNumber), new PhoneNumber("+12054798286"), message).create();

        System.out.println("SMS sent successfully to: " + recipientPhoneNumber);
    }
}
