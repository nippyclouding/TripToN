package server.TripToN;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import server.TripToN.AiResponse.client.GeminiClient;

@SpringBootTest
@ActiveProfiles("test")
class TripToNApplicationTests {


	@MockitoBean
	private GeminiClient geminiClient;


	@Test
	void contextLoads() {
		// Spring Context가 정상적으로 로드되는지 확인하는 기본 테스트
	}

}
