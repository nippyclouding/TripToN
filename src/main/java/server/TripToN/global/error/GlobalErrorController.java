package server.TripToN.global.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        HttpStatus status = resolveStatus(request);
        String message = resolveMessage(request, status);

        model.addAttribute("statusCode", status.value());
        model.addAttribute("statusReason", status.getReasonPhrase());
        model.addAttribute("errorCode", status.name());
        model.addAttribute("errorMessage", message);
        model.addAttribute("requestPath", resolvePath(request));

        return status.is4xxClientError() ? "40x" : "50x";
    }

    private HttpStatus resolveStatus(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode instanceof Integer code) {
            HttpStatus status = HttpStatus.resolve(code);
            if (status != null) {
                return status;
            }
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveMessage(HttpServletRequest request, HttpStatus status) {
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if (message instanceof String text && !text.isBlank()) {
            return text;
        }

        return status.is4xxClientError()
                ? "요청한 페이지를 찾을 수 없거나 접근할 수 없습니다."
                : "서버에서 요청을 처리하는 중 문제가 발생했습니다.";
    }

    private String resolvePath(HttpServletRequest request) {
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (path instanceof String text && !text.isBlank()) {
            return text;
        }

        return request.getRequestURI();
    }
}
