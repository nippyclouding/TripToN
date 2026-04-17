package server.TripToN.concern.dto;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String concernTitle;
    @NotNull
    private String concernContent;
    private boolean isLocked;

}
