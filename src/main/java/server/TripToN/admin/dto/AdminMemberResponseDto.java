package server.TripToN.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.TripToN.member.entity.Member;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberResponseDto {
    private Long memberId;
    private String memberEmail;
    private String memberNickname;
    private LocalDateTime createdAt;

    public static AdminMemberResponseDto from(Member member) {
        return AdminMemberResponseDto.builder()
                .memberId(member.getMemberId())
                .memberEmail(member.getMemberEmail())
                .memberNickname(member.getMemberNickname())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
