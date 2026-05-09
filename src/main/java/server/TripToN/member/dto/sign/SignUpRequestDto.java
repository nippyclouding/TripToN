package server.TripToN.member.dto.sign;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String memberLoginPassword;
    private String memberLoginPasswordConfirm;

    private String memberEmail;

    private String memberNickName;
}
