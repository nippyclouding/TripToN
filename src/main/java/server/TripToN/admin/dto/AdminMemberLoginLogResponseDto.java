package server.TripToN.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.TripToN.member.entity.MemberLoginLog;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberLoginLogResponseDto {
    private Long memberLoginLogId;
    private LocalDateTime createdAt;
    private Boolean loginStatus;
    private String loginFailureReason;
    private String loginTryId;
    private String loginMemberNickname;
    private String loginTryIp;

    public static AdminMemberLoginLogResponseDto from(MemberLoginLog log) {
        return AdminMemberLoginLogResponseDto.builder()
                .memberLoginLogId(log.getMember_login_log_id())
                .createdAt(log.getCreatedAt())
                .loginStatus(log.getLoginStatus())
                .loginFailureReason(log.getLoginFailureReason())
                .loginTryId(log.getLoginTryId())
                .loginMemberNickname(log.getLoginMemberNickname())
                .loginTryIp(log.getLoginTryIp())
                .build();
    }
}
