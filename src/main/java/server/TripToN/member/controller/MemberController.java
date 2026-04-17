package server.TripToN.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.global.util.Const;
import server.TripToN.member.dto.MyPageResponseDto;
import server.TripToN.member.dto.SignInRequestDto;
import server.TripToN.member.dto.SignUpRequestDto;
import server.TripToN.member.entity.Member;
import server.TripToN.member.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;


    // 회원가입
    @PostMapping("/signUp")
    public String signUp(SignUpRequestDto dto, RedirectAttributes attributes) {
        try {
            memberService.signUp(dto);
            attributes.addFlashAttribute("success", "회원가입이 완료됐습니다. 로그인해주세요.");
        } catch (Exception e) {
            log.error("회원가입 실패: {}", e.getMessage(), e);
            attributes.addFlashAttribute("fail", "회원가입에 실패했습니다: " + e.getMessage());
        }
        return "redirect:/";
    }

    // 로그인
    @PostMapping("/signIn")
    public String signIn(SignInRequestDto dto, HttpSession session, RedirectAttributes attributes) {
        Member member = memberService.signIn(dto);
        if (member != null) {
            session.setAttribute(Const.MEMBER_SESSION_KEY, member.getMemberId());
            attributes.addFlashAttribute("success", "로그인 되었습니다.");
        } else {
            attributes.addFlashAttribute("fail", "이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return "redirect:/";
    }

    // 로그아웃
    @PostMapping("/signOut")
    public String signOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 회원탈퇴

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
        model.addAttribute("concernPage", concernPage);
        model.addAttribute("commentPage", commentPage);
        model.addAttribute("concernLikePage", concernLikePage);
        model.addAttribute("commentLikePage", commentLikePage);
        return "myPage";
    }
}
