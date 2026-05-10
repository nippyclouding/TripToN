package server.TripToN.member.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.TripToN.member.dto.sign.SignInRequestDto;
import server.TripToN.member.dto.sign.SignUpRequestDto;
import server.TripToN.member.entity.Member;
import server.TripToN.member.entity.MemberLoginLog;
import server.TripToN.member.repository.MemberLoginLogRepository;
import server.TripToN.member.repository.MemberRepository;
import server.TripToN.global.error.BusinessException;
import server.TripToN.global.error.ErrorCode;
import server.TripToN.global.util.BcryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberAuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberLoginLogRepository memberLoginLogRepository;

    @InjectMocks
    private MemberAuthService memberAuthService;

    @Test
    void signUp_success_savesMember() {
        // given
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .memberEmail("user@tripton.com")
                .memberNickName("traveler")
                .memberLoginPassword("password")
                .memberLoginPasswordConfirm("password")
                .build();

        when(memberRepository.findByMemberEmail(dto.getMemberEmail())).thenReturn(Optional.empty());
        when(memberRepository.findByMemberNickname(dto.getMemberNickName())).thenReturn(Optional.empty());

        // when
        memberAuthService.signUp(dto);

        // then
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(captor.capture());
        assertThat(captor.getValue().getMemberEmail()).isEqualTo("user@tripton.com");
        assertThat(captor.getValue().getMemberNickname()).isEqualTo("traveler");
        assertThat(captor.getValue().getMemberLoginPassword()).isNotEqualTo("password");
        assertThat(BcryptPasswordEncoder.matches("password", captor.getValue().getMemberLoginPassword())).isTrue();
    }

    @Test
    void signUp_passwordConfirmMismatch_throwsException() {
        // given
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .memberEmail("user@tripton.com")
                .memberNickName("traveler")
                .memberLoginPassword("password")
                .memberLoginPasswordConfirm("wrong")
                .build();

        // when / then
        assertThatThrownBy(() -> memberAuthService.signUp(dto))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void signUp_duplicateEmail_throwsException() {
        // given
        SignUpRequestDto dto = SignUpRequestDto.builder()
                .memberEmail("user@tripton.com")
                .memberNickName("traveler")
                .memberLoginPassword("password")
                .memberLoginPasswordConfirm("password")
                .build();
        when(memberRepository.findByMemberEmail(dto.getMemberEmail()))
                .thenReturn(Optional.of(Member.builder().build()));

        // when / then
        assertThatThrownBy(() -> memberAuthService.signUp(dto))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void signIn_success_returnsMemberAndSavesSuccessLogWithIp() {
        // given
        SignInRequestDto dto = SignInRequestDto.builder()
                .memberEmail("user@tripton.com")
                .memberLoginPassword("password")
                .build();
        Member member = Member.builder()
                .memberId(1L)
                .memberEmail("user@tripton.com")
                .memberNickname("traveler")
                .memberLoginPassword(BcryptPasswordEncoder.encode("password"))
                .build();
        when(memberRepository.findByMemberEmail(dto.getMemberEmail())).thenReturn(Optional.of(member));

        // when
        Member result = memberAuthService.signIn(dto, "2001:db8::1");

        // then
        assertThat(result).isEqualTo(member);
        ArgumentCaptor<MemberLoginLog> captor = ArgumentCaptor.forClass(MemberLoginLog.class);
        verify(memberLoginLogRepository).save(captor.capture());
        assertThat(captor.getValue().getLoginStatus()).isTrue();
        assertThat(captor.getValue().getLoginTryIp()).isEqualTo("2001:db8::1");
        assertThat(captor.getValue().getLoginFailureReason()).isNull();
    }

    @Test
    void signIn_unknownEmail_returnsNullAndSavesFailureLogWithIp() {
        // given
        SignInRequestDto dto = SignInRequestDto.builder()
                .memberEmail("missing@tripton.com")
                .memberLoginPassword("password")
                .build();
        when(memberRepository.findByMemberEmail(dto.getMemberEmail())).thenReturn(Optional.empty());

        // when
        Member result = memberAuthService.signIn(dto, "192.168.0.10");

        // then
        assertThat(result).isNull();
        ArgumentCaptor<MemberLoginLog> captor = ArgumentCaptor.forClass(MemberLoginLog.class);
        verify(memberLoginLogRepository).save(captor.capture());
        assertThat(captor.getValue().getLoginStatus()).isFalse();
        assertThat(captor.getValue().getLoginTryIp()).isEqualTo("192.168.0.10");
        assertThat(captor.getValue().getLoginFailureReason()).isEqualTo("존재하지 않는 이메일로 로그인 시도");
    }

    @Test
    void signIn_wrongPassword_returnsNullAndSavesFailureLogWithIp() {
        // given
        SignInRequestDto dto = SignInRequestDto.builder()
                .memberEmail("user@tripton.com")
                .memberLoginPassword("wrong")
                .build();
        Member member = Member.builder()
                .memberId(1L)
                .memberEmail("user@tripton.com")
                .memberNickname("traveler")
                .memberLoginPassword(BcryptPasswordEncoder.encode("password"))
                .build();
        when(memberRepository.findByMemberEmail(dto.getMemberEmail())).thenReturn(Optional.of(member));

        // when
        Member result = memberAuthService.signIn(dto, "192.168.0.20");

        // then
        assertThat(result).isNull();
        ArgumentCaptor<MemberLoginLog> captor = ArgumentCaptor.forClass(MemberLoginLog.class);
        verify(memberLoginLogRepository).save(captor.capture());
        assertThat(captor.getValue().getLoginStatus()).isFalse();
        assertThat(captor.getValue().getLoginTryIp()).isEqualTo("192.168.0.20");
        assertThat(captor.getValue().getLoginFailureReason()).isEqualTo("패스워드 오류");
    }
}
