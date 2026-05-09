package server.TripToN.AiResponse.client;

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

        Candidate candidate = candidates.get(0);
        if (candidate == null || candidate.getContent() == null) return null;

        List<Part> parts = candidate.getContent().getParts();
        if (parts == null || parts.isEmpty() || parts.get(0) == null) return null;

        return parts.get(0).getText();
    }
}
