package TripToN.TripToN.AiResponse.client;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeminiResponse {
    private List<Candidate> candidates;

    @Data
    public static class Candidate {
        private Content content;
    }

    @Data
    public static class Content {
        private List<Part> parts;
    }

    @Data
    public static class Part {
        private String text;
    }

    public String extractText() {
        if (candidates == null || candidates.isEmpty()) return null;
        List<Part> parts = candidates.get(0).getContent().getParts();
        if (parts == null || parts.isEmpty()) return null;
        return parts.get(0).getText();
    }
}
