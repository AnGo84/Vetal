package ua.com.vetal.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.com.vetal.email.EmailMessage;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
/*@WebAppConfiguration*/
@SpringBootTest
@Slf4j
public class MailServiceImpTest {

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

    @Autowired
    private MailServiceImp mailServiceImp;

    @BeforeEach
    public void beforeEach() throws Throwable {
        smtpServerRule.before();
    }

    @AfterEach
    public void afterEach() throws Throwable {
        smtpServerRule.after();
    }

    @Test
    public void shouldSendEmailMessage() throws MessagingException, IOException {
        //log.info("SmtpServer: {}",smtpServerRule. .getSmtp().getServerSetup());
        /*Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo("info@memorynotfound.com");
        mail.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
        mail.setContent("We show how to write Integration Tests using Spring and GreenMail.");*/
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setFrom("no-reply@memorynotfound.com");
        emailMessage.setTo("info@memorynotfound.com");
        emailMessage.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
        emailMessage.setText("We show how to write Integration Tests using Spring and GreenMail.");

        mailServiceImp.sendEmail(emailMessage);

        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage current = receivedMessages[0];
        log.info("Subject: {}", current.getSubject());
        log.info("From: {}", current.getFrom());
        log.info("To: {}", current.getAllRecipients());
        log.info("Content: {}", current.getContent());
        log.info("Content: {}", getTextFromMessage(current));
        log.info("Content: {}", String.valueOf(current.getContent().toString()));
        log.info("Instance  ofContent: {}", (current.getContent().getClass()));


        assertEquals(emailMessage.getSubject(), current.getSubject());
        assertEquals(emailMessage.getTo(), current.getAllRecipients()[0].toString());
        assertTrue(getTextFromMessage(current).contains(emailMessage.getText()));

    }

    @Test
    public void shouldSendSingleMail() throws MessagingException, IOException {
        //log.info("SmtpServer: {}",smtpServerRule. .getSmtp().getServerSetup());
        /*Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo("info@memorynotfound.com");
        mail.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
        mail.setContent("We show how to write Integration Tests using Spring and GreenMail.");*/
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setFrom("no-reply@memorynotfound.com");
        emailMessage.setTo("info@memorynotfound.com");
        emailMessage.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
        emailMessage.setText("We show how to write Integration Tests using Spring and GreenMail.");

        mailServiceImp.sendEmail("no-reply@memorynotfound.com",
                "info@memorynotfound.com",
                "Spring Mail Integration Testing with JUnit and GreenMail Example",
                "We show how to write Integration Tests using Spring and GreenMail.");

        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage current = receivedMessages[0];
        log.info("Subject: {}", current.getSubject());
        log.info("From: {}", current.getFrom());
        log.info("To: {}", current.getAllRecipients());
        log.info("Content: {}", current.getContent());
        log.info("Content: {}", current.getContent());
        log.info("Content: {}", String.valueOf(current.getContent().toString()));
        log.info("Instance  ofContent: {}", (current.getContent().getClass()));


        assertEquals(emailMessage.getSubject(), current.getSubject());
        assertEquals(emailMessage.getTo(), current.getAllRecipients()[0].toString());
        assertTrue(String.valueOf(current.getContent()).contains(emailMessage.getText()));

    }

    private String getTextFromMessage(MimeMessage message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws IOException, MessagingException {

        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            // alternatives appear in an order of increasing
            // faithfulness to the original content. Customize as req'd.
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    private String getTextFromBodyPart(
            BodyPart bodyPart) throws IOException, MessagingException {

        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return result;
    }
}