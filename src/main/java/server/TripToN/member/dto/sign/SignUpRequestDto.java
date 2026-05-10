package server.TripToN.member.dto.sign;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String memberLoginPassword;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String memberLoginPasswordConfirm;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String memberEmail;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String memberNickName;
}
