package server.TripToN.AiResponse.dto;

import jakarta.persistence.Column;
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

    // concern 테이블 데이터
    private String concernTitle;    // 제목
    private String concernContent;  // 답변
}
