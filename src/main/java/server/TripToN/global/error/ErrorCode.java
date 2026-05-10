package server.TripToN.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ── 시스템 ──────────────────────────────────────────────
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "입력값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."),

    // ── 인증/인가 ────────────────────────────────────────────
    SESSION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "SESSION_NOT_FOUND", "로그인이 필요합니다."),

    // ── 회원 ──────────────────────────────────────────────
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "NICKNAME_ALREADY_EXISTS", "이미 사용 중인 닉네임입니다."),
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD_CONFIRM_MISMATCH", "패스워드 입력이 잘못되었습니다."),

    // ── 고민/댓글 ──────────────────────────────────────────────
    CONCERN_NOT_FOUND(HttpStatus.NOT_FOUND, "CONCERN_NOT_FOUND", "해당 고민이 존재하지 않습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "해당 댓글이 존재하지 않습니다."),

    // ── 권한 ──────────────────────────────────────────────
    WRONG_ACCESS_UPDATE(HttpStatus.FORBIDDEN, "WRONG_ACCESS_UPDATE", "수정 권한이 없습니다."),
    WRONG_ACCESS_DELETE(HttpStatus.FORBIDDEN, "WRONG_ACCESS_DELETE", "삭제 권한이 없습니다.");

    // ── 파일 ──────────────────────────────────────────────────
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
