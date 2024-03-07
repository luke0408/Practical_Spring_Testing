package cafekiosk.spring.client;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSendClient {

	public boolean sendMail(String fromMail, String toMail, String subject, String content) {
		log.info("매일 전송");
		throw new IllegalArgumentException("메일 전송에 실패했습니다.");
	}

}
