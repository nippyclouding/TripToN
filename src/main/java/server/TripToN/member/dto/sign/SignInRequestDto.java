package server.TripToN.member.dto.sign;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {
    private String memberNickName;
    private String memberLoginPassword;
    private String memberEmail;
}
