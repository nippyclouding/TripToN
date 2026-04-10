package TripToN.TripToN.AiResponse.service;

import TripToN.TripToN.AiResponse.entity.AiResponse;
import TripToN.TripToN.AiResponse.repository.AiResponseRepository;
import TripToN.TripToN.concern.entity.Concern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AiResponseService {

    private final AiResponseRepository aiResponseRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AiResponse getResponse(Concern concern) {
        // 1. 대답 얻기

        // 2. 대답 저장

        // 3. 대답 리턴
    }
}
