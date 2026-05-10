package server.TripToN.concern.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.TripToN.concern.dto.*;
import server.TripToN.concern.service.ConcernService;
import server.TripToN.AiResponse.dto.AiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import server.TripToN.concernLike.service.ConcernLikeService;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.global.util.Const;



@Controller
@RequiredArgsConstructor
@RequestMapping("/concern")
public class ConcernController {

    private final ConcernService concernService;
    private final ConcernLikeService concernLikeService;

    // 고민을 받아와서 저장, 응답 전달
    @PostMapping
    public String saveConcern(@Valid @ModelAttribute ConcernRequestDto dto,
                              Model model,
                              HttpSession session) {
        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (memberId == null) return "redirect:/";
        AiResponseDto aiResponseDto = concernService.saveConcernAndGetAiResponse(dto, memberId);
        model.addAttribute("aiResponseDto", aiResponseDto);
        return "info";
    }


    // 고민 전체 리스트
    @GetMapping
    public String getConcerns(Model model,
                              @RequestParam(defaultValue = "0") int page) {
        Page<ConcernResponseDto> concernPage = concernService.getConcerns(page);
        model.addAttribute("concernResponseDto", concernPage.getContent());
        model.addAttribute("totalPages", concernPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "result";
    }

    // 고민 상세 조회
    @GetMapping("/{concernId}")
    public String getConcernDetail(@PathVariable Long concernId, Model model, HttpSession session) {
        Long loginMemberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        ConcernDetailResponseDto dto = concernService.getConcernDetail(concernId, loginMemberId); // 고민, 응답, 댓글, 회원 정보

        boolean isLiked = loginMemberId != null && concernLikeService.isLiked(loginMemberId, concernId);
        long likeCount = concernLikeService.getLikeCount(concernId);

        // 고민 데이터 : dto로 렌더링
        model.addAttribute(dto);
        // 요청자 세션 기준 뷰 렌더링 : 필드로 전송
        model.addAttribute("loginMemberId", loginMemberId);
        model.addAttribute("concernId", concernId);
        model.addAttribute("isLiked", isLiked);
        model.addAttribute("likeCount", likeCount);
        return "detail";
    }

    // 고민 상세 페이지 - 수정
    @PostMapping("/{concernId}/update")
    public String updateConcern(@PathVariable Long concernId, @Valid @ModelAttribute ConcernUpdateRequestDto reqDto,
                                HttpSession session) {
        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (memberId == null) return "redirect:/";
        concernService.updateConcern(concernId, memberId, reqDto);
        return "redirect:/concern/" + concernId;
    }

    // 고민 상세 페이지 - 삭제
    @PostMapping("/{concernId}/remove")
    public String removeConcern(@PathVariable Long concernId, HttpSession session) {
        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (memberId == null) return "redirect:/";
        concernService.removeConcern(concernId, memberId);
        return "redirect:/concern";
    }

    // 고민 좋아요 등록
    @ResponseBody
    @PostMapping("/api/{concernId}/like")
    public ResponseEntity<Void> addConcernLike(@PathVariable Long concernId, HttpSession session) {
        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (memberId == null) throw new BusinessException(ErrorCode.SESSION_NOT_FOUND);

        concernLikeService.toggleLike(memberId, concernId);

        return ResponseEntity.ok().build();
    }

}
