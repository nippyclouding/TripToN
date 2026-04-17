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
    ENTITY_NOT_FOUNT_ERROR(HttpStatus.NOT_FOUND, "CANNOT_FIND_ENTITY", "잘못된 접근입니다."),

    // ── 인증/인가 ────────────────────────────────────────────
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "만료된 토큰입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "LOGIN_FAILED", "아이디 또는 비밀번호가 올바르지 않습니다."),

    // ── 회원 ──────────────────────────────────────────────
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "INVALID_PASSWORD", "비밀번호가 올바르지 않습니다."),
    WRONG_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "WRONG_CURRENT_PASSWORD", "현재 비밀번호가 올바르지 않습니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "INSUFFICIENT_BALANCE", "원화 잔고가 부족합니다."),
    INSUFFICIENT_TOKEN_BALANCE(HttpStatus.BAD_REQUEST, "INSUFFICIENT_TOKEN_BALANCE", "보유 토큰이 부족합니다."),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "INVALID_AMOUNT", "금액은 0보다 커야 합니다."),



    SESSION_NOT_FOUND(HttpStatus.BAD_REQUEST, "NO_SESSION ERROR", "현재 세션이 존재하지 않습니다."),
    WRONG_ACCESS_UPDATE(HttpStatus.BAD_REQUEST, "WRONG_UPDATE_ACCESS_BY_OTHER_USER", "다른 회원이 수정을 시도합니다."), // 댓글, 고민 수정 오류
    WRONG_ACCESS_DELETE(HttpStatus.BAD_REQUEST, "WRONG_DELETE_ACCESS_BY_OTHER_USER", "다른 회원이 삭제를 시도합니다."), // 댓글, 고민 삭제 오류
    CONCERN_NOT_FOUND(HttpStatus.BAD_REQUEST, "CONCERN_NOT_FOUND", "해당 고민이 존재하지 않습니다.");

    // ── 파일 ──────────────────────────────────────────────────
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
