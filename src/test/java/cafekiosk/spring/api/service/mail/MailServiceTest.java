package cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cafekiosk.spring.client.MailSendClient;
import cafekiosk.spring.domain.histotory.mail.MailSendHistory;
import cafekiosk.spring.domain.histotory.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@Mock
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks // @Mock으로 설정된 객체를 주입받는다.
	private MailService mailService;

	@Test
	@DisplayName("메일 전송 테스트")
	void senMail() {
		// given
		given(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString()))
				.willReturn(true);

		// when
		boolean result = mailService.sendMail("", "", "", "");

		// then
		assertThat(result).isTrue();
		verify(mailSendHistoryRepository, times(1))
				.save(any(MailSendHistory.class));
	}

}
