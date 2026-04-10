package server.TripToN.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.TripToN.member.dto.SignInRequestDto;
import server.TripToN.member.dto.SignUpRequestDto;
import server.TripToN.member.entity.Member;
import server.TripToN.member.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void signUp(SignUpRequestDto dto) {
        Member member = Member.builder()
                .memberEmail(dto.getMemberEmail())
                .memberLoginPassword(dto.getMemberLoginPassword())
                .memberNickName(dto.getMemberNickName())
                .build();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member signIn(SignInRequestDto dto) {
        Optional<Member> memberOpt = memberRepository.findByMemberEmail(dto.getMemberEmail());

        if (memberOpt.isEmpty()) return null;

        Member member = memberOpt.get();
        if (member.getMemberLoginPassword().equals(dto.getMemberLoginPassword())) return member;
        return null;
    }
}
