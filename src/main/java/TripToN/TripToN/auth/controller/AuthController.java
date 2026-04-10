package TripToN.TripToN.auth.controller;

import TripToN.TripToN.global.security.JwtUtil;
import TripToN.TripToN.member.entity.Member;
import TripToN.TripToN.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String loginId,
                                   @RequestParam String password) {

        Member member = memberRepository.findByMemberLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(password, member.getMemberLoginPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "비밀번호가 틀렸습니다."));
        }

        String token = jwtUtil.generate(member.getMemberId(), member.getMemberLoginId());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
