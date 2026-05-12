package server.TripToN.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.global.util.Const;
import server.TripToN.member.dto.mypage.MyPageCommentLikeDto;
import server.TripToN.member.dto.mypage.MyPageCommentResponseDto;
import server.TripToN.member.dto.mypage.MyPageConcernLikeDto;
import server.TripToN.member.dto.mypage.MyPageConcernResponseDto;
import server.TripToN.member.dto.mypage.MyPageProfileResponseDto;
import server.TripToN.member.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(HttpSession session) {
        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (memberId == null) return "redirect:/";

        return "myPage";
    }

    @GetMapping("/myPage/api/profile")
    public ResponseEntity<MyPageProfileResponseDto> getMyPageProfile(HttpSession session) {
        Long memberId = getLoginMemberId(session);

        return ResponseEntity.ok(memberService.getMyPageProfile(memberId));
    }

    @GetMapping("/myPage/api/concerns")
    public ResponseEntity<Page<MyPageConcernResponseDto>> getMyConcerns(HttpSession session,
                                                                        @RequestParam(defaultValue = "0") int page) {
        Long memberId = getLoginMemberId(session);

        return ResponseEntity.ok(memberService.getMyConcerns(memberId, page));
    }

    @GetMapping("/myPage/api/comments")
    public ResponseEntity<Page<MyPageCommentResponseDto>> getMyComments(HttpSession session,
                                                                        @RequestParam(defaultValue = "0") int page) {
        Long memberId = getLoginMemberId(session);

        return ResponseEntity.ok(memberService.getMyComments(memberId, page));
    }

    @GetMapping("/myPage/api/concern-likes")
    public ResponseEntity<Page<MyPageConcernLikeDto>> getMyConcernLikes(HttpSession session,
                                                                        @RequestParam(defaultValue = "0") int page) {
        Long memberId = getLoginMemberId(session);

        return ResponseEntity.ok(memberService.getMyConcernLikes(memberId, page));
    }

    @GetMapping("/myPage/api/comment-likes")
    public ResponseEntity<Page<MyPageCommentLikeDto>> getMyCommentLikes(HttpSession session,
                                                                        @RequestParam(defaultValue = "0") int page) {
        Long memberId = getLoginMemberId(session);

        return ResponseEntity.ok(memberService.getMyCommentLikes(memberId, page));
    }

    private Long getLoginMemberId(HttpSession session) {
        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (memberId == null) throw new BusinessException(ErrorCode.SESSION_NOT_FOUND);
        return memberId;
    }
}
