package server.TripToN.global.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    protected Object handleBindException(BindException e, HttpServletRequest request) {
        log.error("handleBindException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(),
                e.getBindingResult());
        return errorResponse(request, HttpStatus.BAD_REQUEST, errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected Object handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                              HttpServletRequest request) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(),
                e.getMessage());
        return errorResponse(request, HttpStatus.BAD_REQUEST, errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                 HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED.toString(),
                e.getMessage());
        return errorResponse(request, HttpStatus.METHOD_NOT_ALLOWED, errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected Object handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        log.error("DataIntegrityViolationException", e);
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.EMAIL_ALREADY_EXISTS.getErrorCode(),
                ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
        return errorResponse(request, HttpStatus.CONFLICT, errorResponse);
    }

    @ExceptionHandler(value = {BusinessException.class})
    protected Object handleConflict(BusinessException e, HttpServletRequest request) {
        log.error("BusinessException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode().getErrorCode(), e.getMessage());
        return errorResponse(request, e.getErrorCode().getHttpStatus(), errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected Object handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("IllegalArgumentException", e);
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.INVALID_INPUT_VALUE.getErrorCode(),
                e.getMessage());
        return errorResponse(request, HttpStatus.BAD_REQUEST, errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected Object handleException(Exception e, HttpServletRequest request) {
        log.error("Exception", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "서버 오류가 발생했습니다.");
        return errorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);
    }

    private Object errorResponse(HttpServletRequest request, HttpStatus status, ErrorResponse errorResponse) {
        if (!acceptsHtml(request)) {
            return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
        }

        ModelAndView modelAndView = new ModelAndView(status.is4xxClientError() ? "40x" : "50x");
        modelAndView.setStatus(status);
        modelAndView.addObject("statusCode", status.value());
        modelAndView.addObject("statusReason", status.getReasonPhrase());
        modelAndView.addObject("errorCode", errorResponse.getErrorCode());
        modelAndView.addObject("errorMessage", errorResponse.getErrorMessage());
        modelAndView.addObject("requestPath", request.getRequestURI());
        return modelAndView;
    }

    private boolean acceptsHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept == null || accept.contains(MediaType.TEXT_HTML_VALUE);
    }
}
