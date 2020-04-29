package ua.com.vetal.email;

import ua.com.vetal.entity.Task;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.util.ArrayList;
import java.util.List;

public class EmailMessageBuilder {
	public EmailMessage build(Task task, String subject, String text) {
		EmailMessage emailMessage = new EmailMessage();
		List<EmailAttachment> attachments = new ArrayList<>();

		if (task.getDbFile() != null) {
			DataSource source = new ByteArrayDataSource(task.getDbFile().getData(), task.getDbFile().getFileType());
			attachments.add(new EmailAttachment(task.getDbFile().getFileName(), source));
		}
		emailMessage.setFrom(task.getManager().getEmail());
		emailMessage.setTo(task.getContractor().getEmail());
		emailMessage.setSubject(subject + task.getNumber());
		emailMessage.setText(text);
		emailMessage.setAttachments(attachments);
		return emailMessage;
	}
}
