package server.TripToN.concern.controller;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;
import server.TripToN.concern.dto.ConcernRequestDto;
import server.TripToN.concern.dto.ConcernResponseDto;
import server.TripToN.concern.service.ConcernService;
import server.TripToN.AiResponse.dto.AiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/concern")
public class ConcernController {

    private final ConcernService concernService;

    // 고민 전체 리스트
    @GetMapping
    public String getConcerns(Model model,
                              @RequestParam(defaultValue = "0") int page) {
        List<ConcernResponseDto> dtos = concernService.getConcerns(page);
        model.addAttribute("concernResponseDto", dtos);
        return "6_result";
    }

    // 고민을 받아와서 저장, 응답 전달
    @PostMapping
    public String saveConcern(@Valid @ModelAttribute ConcernRequestDto dto, Model model) {

        AiResponseDto aiResponseDto = concernService.saveConcernAndGetAiResponse(dto);
        model.addAttribute("aiResponseDto", aiResponseDto);
        return "redirect:5_info";
    }

}
