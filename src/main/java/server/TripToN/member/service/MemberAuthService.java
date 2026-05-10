package server.TripToN.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.TripToN.member.dto.sign.SignInRequestDto;
import server.TripToN.member.dto.sign.SignUpRequestDto;
import server.TripToN.member.entity.Member;
import server.TripToN.member.entity.MemberLoginLog;
import server.TripToN.member.repository.MemberLoginLogRepository;
import server.TripToN.member.repository.MemberRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final MemberLoginLogRepository memberLoginLogRepository;

    public void signUp(SignUpRequestDto dto) {
        if (!dto.getMemberLoginPassword().equals(dto.getMemberLoginPasswordConfirm())) {
            throw new BusinessException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }
        if (memberRepository.findByMemberEmail(dto.getMemberEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (memberRepository.findByMemberNickname(dto.getMemberNickName()).isPresent()) {
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        Member member = Member.builder()
                .memberEmail(dto.getMemberEmail())
                .memberLoginPassword(dto.getMemberLoginPassword())
                .memberNickname(dto.getMemberNickName())
                .build();
        memberRepository.save(member);
    }

    @Transactional
    public Member signIn(SignInRequestDto dto, String loginTryIp) {
        Optional<Member> memberOpt = memberRepository.findByMemberEmail(dto.getMemberEmail());

        if (memberOpt.isEmpty()) {
            saveLoginLog(dto.getMemberEmail(), null, false, "존재하지 않는 이메일로 로그인 시도", loginTryIp);
            return null;
        }

        Member member = memberOpt.get();
        if (member.getMemberLoginPassword().equals(dto.getMemberLoginPassword())) {
            saveLoginLog(member.getMemberEmail(), member.getMemberNickname(), true, null, loginTryIp);
            return member;
        }

        saveLoginLog(member.getMemberEmail(), member.getMemberNickname(), false, "패스워드 오류", loginTryIp);
        return null;
    }

    private void saveLoginLog(String tryId, String nickname, boolean status, String reason, String loginTryIp) {
        memberLoginLogRepository.save(
                MemberLoginLog.builder()
                        .loginTryId(tryId)
                        .loginMemberNickname(nickname)
                        .loginStatus(status)
                        .loginFailureReason(reason)
                        .loginTryIp(loginTryIp)
                        .build()
        );
    }
}
