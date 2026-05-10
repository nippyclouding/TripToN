package server.TripToN.concern.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import server.TripToN.comment.dto.CommentResponseDto;
import server.TripToN.concern.entity.LuggageType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcernUpdateRequestDto {

    // 고민 정보
    @NotBlank(message = "고민 제목을 입력해주세요.")
    private String concernTitle;

    @NotBlank(message = "고민 내용을 입력해주세요.")
    private String concernContent;

}
