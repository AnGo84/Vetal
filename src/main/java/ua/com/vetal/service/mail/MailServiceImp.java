package ua.com.vetal.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ua.com.vetal.email.EmailAttachment;
import ua.com.vetal.email.EmailMessage;
import ua.com.vetal.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImp implements EmailService {

	@Qualifier("getJavaMailSender")
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(String emailFrom, String emailTo, String subject, String message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(emailFrom);
		mailMessage.setTo(emailTo);

		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		javaMailSender.send(mailMessage);
	}

	/*public void sendMessageWithAttachment(
			String from, String to, String subject, String text, List<EmailAttachment> attachments) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		if (attachments != null && !attachments.isEmpty()) {
			for (EmailAttachment attachment : attachments) {
				helper.addAttachment(attachment.getName(), attachment.getDataSource());
			}
		}
		javaMailSender.send(message);
	}*/

	public void sendEmail(EmailMessage emailMessage) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		fillMimeMessageHelperFromEmailMessage(emailMessage, helper);

		javaMailSender.send(message);
	}

	private void fillMimeMessageHelperFromEmailMessage(EmailMessage emailMessage, MimeMessageHelper helper) throws MessagingException {
		helper.setFrom(emailMessage.getFrom());
		helper.setTo(emailMessage.getTo());
		helper.setSubject(emailMessage.getSubject());
		helper.setText(emailMessage.getText());
		if (emailMessage.getAttachments() != null && !emailMessage.getAttachments().isEmpty()) {
			for (EmailAttachment attachment : emailMessage.getAttachments()) {
				helper.addAttachment(attachment.getName(), attachment.getDataSource());
			}
		}
	}
}
