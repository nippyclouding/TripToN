package server.TripToN.concern.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.TripToN.comment.dto.CommentResponseDto;
import server.TripToN.comment.service.CommentService;
import server.TripToN.concern.dto.ConcernDetailResponseDto;
import server.TripToN.concern.dto.ConcernRequestDto;
import server.TripToN.concern.dto.ConcernResponseDto;
import server.TripToN.concern.service.ConcernService;
import server.TripToN.AiResponse.dto.AiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import server.TripToN.global.util.Const;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/concern")
public class ConcernController {

    private final ConcernService concernService;

    // 고민을 받아와서 저장, 응답 전달
    @PostMapping
    public String saveConcern(@Valid @ModelAttribute ConcernRequestDto dto,
                              Model model,
                              HttpSession session) {
        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        AiResponseDto aiResponseDto = concernService.saveConcernAndGetAiResponse(dto, memberId);
        model.addAttribute("aiResponseDto", aiResponseDto);
        return "5_info";
    }


    // 고민 전체 리스트
    @GetMapping
    public String getConcerns(Model model,
                              @RequestParam(defaultValue = "0") int page) {
        Page<ConcernResponseDto> concernPage = concernService.getConcerns(page);
        model.addAttribute("concernResponseDto", concernPage.getContent());
        model.addAttribute("totalPages", concernPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "6_result";
    }

    // 고민 상세 조회
    @GetMapping("/{concernId}")
    public String getConcernDetail(@PathVariable Long concernId, Model model, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        ConcernDetailResponseDto dto = concernService.getConcernDetail(concernId); // 고민, 응답, 댓글, 회원 정보

        Boolean isAccessAllowed = !dto.getIsLocked() || (loginMemberId != null && loginMemberId.equals(dto.getMemberId()));

        model.addAttribute(dto);
        model.addAttribute("loginMemberId", loginMemberId);
        model.addAttribute("concernId", concernId);
        model.addAttribute("isAccessAllowed", isAccessAllowed);
        return "detail";
    }

    // 고민 상세 페이지 - 수정

    // 고민 상세 페이지 - 삭제


}
