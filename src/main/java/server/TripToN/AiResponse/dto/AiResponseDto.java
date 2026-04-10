package server.TripToN.AiResponse.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiResponseDto {
    // 사용자 고민 응답 결과로 화면에 전달될 dto
    private String responseContent;
    private LocalDateTime createdAt;
}
