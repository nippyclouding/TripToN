package server.TripToN.concern.repository;

import java.time.LocalDateTime;

public interface ConcernAdminProjection {
    Long getConcernId();
    String getConcernTitle();
    String getConcernContent();
    String getLuggageType();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getDeletedAt();
    Long getMemberId();
    String getMemberEmail();
    String getMemberNickname();
}
