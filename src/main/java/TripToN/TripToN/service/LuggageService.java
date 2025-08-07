package TripToN.TripToN.service;



import TripToN.TripToN.domain.luggages.*;
import TripToN.TripToN.domain.response.*;
import TripToN.TripToN.repository.LuggageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LuggageService {

    private final LuggageRepository luggageRepository;



    public List<Luggage> findAll() {
        return luggageRepository.findAll();
    }

    public Luggage findById(Long id) {
        return luggageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("데이터를 찾을 수 없습니다"));
    }

    @Transactional
    public void put(LuggageA luggage) {
        ResponseA response = new ResponseA();
        luggage.setResponse(response);

        if(luggage.getConcern().length()<25){
            luggage.setAnswer(response.getResponses().get(0));
        }
        else if(luggage.getConcern().length()>=25 && luggage.getConcern().length()<50){
            luggage.setAnswer(response.getResponses().get(1));
        }
        else if(luggage.getConcern().length()>=50 && luggage.getConcern().length()<75){
            luggage.setAnswer(response.getResponses().get(2));
        }
        else if(luggage.getConcern().length()>=75 && luggage.getConcern().length()<=100){
            luggage.setAnswer(response.getResponses().get(3));
        }
        luggageRepository.save(luggage);
    }

    @Transactional
    public void put(LuggageB luggage) {
        ResponseB response = new ResponseB();
        luggage.setResponse(response);

        if(luggage.getConcern().length()<25){
            luggage.setAnswer(response.getResponses().get(0));
        }
        else if(luggage.getConcern().length()>=25 && luggage.getConcern().length()<50){
            luggage.setAnswer(response.getResponses().get(1));
        }
        else if(luggage.getConcern().length()>=50 && luggage.getConcern().length()<75){
            luggage.setAnswer(response.getResponses().get(2));
        }
        else if(luggage.getConcern().length()>=75 && luggage.getConcern().length()<=100){
            luggage.setAnswer(response.getResponses().get(3));
        }
        luggageRepository.save(luggage);
    }

    @Transactional
    public void put(LuggageC luggage) {
        ResponseC response = new ResponseC();
        luggage.setResponse(response);

        if(luggage.getConcern().length()<25){
            luggage.setAnswer(response.getResponses().get(0));
        }
        else if(luggage.getConcern().length()>=25 && luggage.getConcern().length()<50){
            luggage.setAnswer(response.getResponses().get(1));
        }
        else if(luggage.getConcern().length()>=50 && luggage.getConcern().length()<75){
            luggage.setAnswer(response.getResponses().get(2));
        }
        else if(luggage.getConcern().length()>=75 && luggage.getConcern().length()<=100){
            luggage.setAnswer(response.getResponses().get(3));
        }
        luggageRepository.save(luggage);
    }

}
