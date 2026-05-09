package server.TripToN.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import server.TripToN.global.util.Const;
import server.TripToN.member.dto.mypage.MyPageResponseDto;
import server.TripToN.member.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 마이페이지
    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model,
                         @RequestParam(defaultValue = "0") int concernPage,
                         @RequestParam(defaultValue = "0") int commentPage,
                         @RequestParam(defaultValue = "0") int concernLikePage,
                         @RequestParam(defaultValue = "0") int commentLikePage) {

        Long memberId = (Long) session.getAttribute(Const.MEMBER_SESSION_KEY);
        if (memberId == null) return "redirect:/";

        MyPageResponseDto dto = memberService.getMyPage(memberId, concernPage, commentPage, concernLikePage, commentLikePage);
        model.addAttribute("myPage", dto);

        // ajax, rest api 로 매 번 요청 시 4번의 불필요한 쿼리를 줄일 수 있다 (향후 개선점)
        model.addAttribute("concernPage", concernPage);
        model.addAttribute("commentPage", commentPage);
        model.addAttribute("concernLikePage", concernLikePage);
        model.addAttribute("commentLikePage", commentLikePage);
        return "myPage";
    }
}
