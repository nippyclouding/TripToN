package server.TripToN.global.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import server.TripToN.member.dto.sign.SignInRequestDto;
import server.TripToN.member.dto.sign.SignUpRequestDto;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    @ModelAttribute("signInRequestDto")
    public SignInRequestDto signInRequestDto() {
        return new SignInRequestDto();
    }

    @ModelAttribute("signUpRequestDto")
    public SignUpRequestDto signUpRequestDto() {
        return new SignUpRequestDto();
    }
}
