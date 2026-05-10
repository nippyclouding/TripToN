package server.TripToN.member.dto.sign;

import jakarta.validation.constraints.NotBlank;

public record SignUpNicknameCheckRequestDto(
        @NotBlank(message = "닉네임을 입력해주세요.")
        String nickname
) {
}
