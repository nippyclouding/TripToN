package server.TripToN.member.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String memberLoginPassword;

    private String memberEmail;

    private String memberNickName;
}
