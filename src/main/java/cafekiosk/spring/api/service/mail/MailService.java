package cafekiosk.spring.api.service.mail;


import org.springframework.stereotype.Service;

import cafekiosk.spring.client.MailSendClient;
import cafekiosk.spring.domain.histotory.mail.MailSendHistory;
import cafekiosk.spring.domain.histotory.mail.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

	private final MailSendClient mailSendClient;
	private final MailSendHistoryRepository mailSendHistoryRepository;

	public boolean sendMail(String fromMail, String toMail, String subject, String content) {

		boolean result = mailSendClient.sendMail(fromMail, toMail, subject, content);

		if (result) {
			mailSendHistoryRepository.save(
					MailSendHistory.builder()
							.fromMail(fromMail)
							.toMail(toMail)
							.subject(subject)
							.content(content)
							.build()
			);
		}

		return result;
	}

}
