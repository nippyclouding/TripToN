package server.TripToN.concern.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import server.TripToN.concern.entity.LuggageType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcernRequestDto {
    // 2 페이지에서 화면에서 받아올 데이터
    @NotBlank(message = "고민 제목을 입력해주세요.")
    private String concernTitle;

    @NotBlank(message = "고민 내용을 입력해주세요.")
    private String concernContent;

    @NotNull(message = "가방 종류를 선택해주세요.")
    private LuggageType luggageType;
}
