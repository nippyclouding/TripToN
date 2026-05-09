package server.TripToN.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import server.TripToN.global.util.Const;
import server.TripToN.member.dto.sign.SignInRequestDto;
import server.TripToN.member.dto.sign.SignUpEmailCheckRequestDto;
import server.TripToN.member.dto.sign.SignUpNicknameCheckRequestDto;
import server.TripToN.member.dto.sign.SignUpRequestDto;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;
import server.TripToN.member.service.MemberAuthService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberAuthController {

    private final MemberAuthService memberAuthService;
    private final MemberRepository memberRepository;


    // 회원가입
    @PostMapping("/signUp")
    public String signUp(@ModelAttribute SignUpRequestDto dto, RedirectAttributes attributes) {
        memberAuthService.signUp(dto);
        attributes.addFlashAttribute("success", "회원가입이 완료됐습니다. 로그인해주세요.");
        return "redirect:/";
    }

    // 회원 가입 시 이메일 검증
    @PostMapping("/check-email-unique")
    public ResponseEntity<Boolean> checkUnique(@RequestBody SignUpEmailCheckRequestDto dto) {
        return ResponseEntity.ok(memberRepository.findByMemberEmail(dto.email()).isEmpty());
    }

    // 회원 가입 시 닉네임 검증
    @PostMapping("/check-nickname-unique")
    public ResponseEntity<Boolean> checkNickname(@RequestBody SignUpNicknameCheckRequestDto dto) {
        return ResponseEntity.ok(memberRepository.findByMemberNickname(dto.nickname()).isEmpty());
    }

    // 로그인
    @PostMapping("/signIn")
    public String signIn(@ModelAttribute SignInRequestDto dto, HttpSession session, RedirectAttributes attributes) {
        Member member = memberAuthService.signIn(dto);
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


}
