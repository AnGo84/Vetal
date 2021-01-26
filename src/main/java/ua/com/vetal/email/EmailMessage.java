package ua.com.vetal.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailMessage {
	private String from;
	private String to;
	private String subject;
	private String text;
	private List<EmailAttachment> attachments;
}
