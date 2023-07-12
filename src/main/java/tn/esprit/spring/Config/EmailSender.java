package tn.esprit.spring.Config;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;
import java.math.BigDecimal;

public class EmailSender {
    public static void sendAccountCreationEmail(String recipientEmail, BigDecimal balance) throws IOException {
        // Create the SendGrid API key
        String apiKey = "YOUR_SENDGRID_API_KEY";

        // Create the SendGrid client
        SendGrid sendGrid = new SendGrid(apiKey);

        // Create the email message
        Email from = new Email("manar.benazaiez@esprit.tn");
        String subject = "Account Created";
        Email to = new Email(recipientEmail);
        Content content = new Content("text/plain", "Your account has been created. Your balance is: " + balance);
        Mail mail = new Mail(from, subject, to, content);

        // Send the email
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
