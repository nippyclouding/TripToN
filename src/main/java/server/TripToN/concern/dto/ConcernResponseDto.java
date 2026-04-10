package server.TripToN.concern.dto;

import lombok.*;
import server.TripToN.concern.entity.Concern;
import server.TripToN.concern.entity.LuggageType;
import server.TripToN.member.entity.Member;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcernResponseDto {
    // 결과 페이지에서 보여줄 고민 리스트
    // concern 테이블
    private Long concernId;

    private String concernTitle;

    private boolean isLocked;

    private LuggageType luggageType;

    // member 테이블 -> fetch join
    private String memberNickName;

    public static ConcernResponseDto from(Concern entity, Member member) {
        return ConcernResponseDto.builder()
                .concernId(entity.getConcernId())
                .concernTitle(entity.getConcernTitle())
                .isLocked(entity.isLocked())
                .luggageType(entity.getLuggageType())
                .memberNickName(member.getMemberNickName())
                .build();
    }

}
