package TripToN.TripToN.service;

import TripToN.TripToN.LuggageRepository;
import TripToN.TripToN.domain.*;

import TripToN.TripToN.service.responseService.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LuggageService {

    private final LuggageRepository luggageRepository;
    private final ResponseService responseService; //구현체 변경 가능, Concern의 response 필드 채우기 전용


    //findAll()
    public List<Luggage> findAll() {
        return luggageRepository.findAll();
    }

    //findById()
    public Luggage findById(Long id) {
        return luggageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("데이터를 찾을 수 없습니다"));
    }

    //save()
    @Transactional(readOnly = false)
    public Luggage saveLuggage(Luggage luggage) {
        // 검증
        if (!luggage.isComplete()) {
            StringBuilder missing = new StringBuilder("Incomplete luggage: ");
            if (luggage.getConcern() == null) missing.append("concern is null, ");
            else if (luggage.getConcern().getResponse() == null) missing.append("response is null, ");

            throw new IllegalStateException(missing.toString());
        }

        log.info("Saving complete luggage with id: {}, type: {}",
                luggage.getLID(), luggage.getLuggageType());

        return luggageRepository.save(luggage);
    }



    // 실제 비즈니스 로직 - Concern에 Response 할당 : AI 응답 등 여러가지 기능
    public String setResponse(Concern concern) {
        String response = responseService.response(concern);
        return response;
    }

}
