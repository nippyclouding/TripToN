package TripToN.TripToN.concern.controller;

import TripToN.TripToN.concern.dto.ConcernRequestDto;
import TripToN.TripToN.concern.service.ConcernService;
import TripToN.TripToN.AiResponse.dto.AiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/concern")
public class ConcernController {

    private final ConcernService concernService;

    @PostMapping
    public String getConcern(@Valid @ModelAttribute ConcernRequestDto dto, Model model) {

        AiResponseDto aiResponseDto = concernService.saveConcernAndGetAiResponse(dto);
        model.addAttribute("aiResponseDto", aiResponseDto);
        return "redirect:5_info";
    }

}
