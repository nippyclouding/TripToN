package server.TripToN.AiResponse.client;

public class GeminiApiException extends RuntimeException {

    private final String code;

    public GeminiApiException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
