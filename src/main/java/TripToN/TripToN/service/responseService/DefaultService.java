package TripToN.TripToN.service.responseService;

import TripToN.TripToN.domain.Concern;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


public class DefaultService implements ResponseService{
    @Override
    public String response(Concern concern) {
        if(concern.getConcern().length()<40) return "당신의 고민은 생각보다 가벼운 고민이군요, 조금의 여유를 가지면 좋을 거에요.";
        else if(concern.getConcern().length()<80) return "당신의 고민은 작지 않은 고민이군요, 산책을 즐기면서 답을 찾아보아요.";
        else return "당신의 고민은 큰 고민이군요, 친구 또는 상담사와 이야기를 해보는 것도 좋을 것 같아요.";
    }
}
