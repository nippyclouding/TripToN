package server.TripToN.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequestDto {
    @NotBlank(message = "관리자 ID를 입력해주세요.")
    private String adminLoginId;
    @NotBlank(message = "관리자 비밀번호를 입력해주세요.")
    private String adminLoginPassword;
}
