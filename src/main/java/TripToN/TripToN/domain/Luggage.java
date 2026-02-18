package TripToN.TripToN.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Luggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LID;

    @Embedded
    private Concern concern;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private LuggageType luggageType;

    // Aggregate Root가 Concern 생성을 관장하는 팩토리 메서드
    public static Luggage create(String userName, String concern, String password, LuggageType luggageType) {
        Luggage luggage = new Luggage();
        luggage.concern = new Concern(userName, concern, password);
        luggage.luggageType = luggageType;
        luggage.dateTime = LocalDateTime.now();
        return luggage;
    }

    // Aggregate Root가 내부 Concern의 응답 할당을 관장
    public void assignResponse(String response) {
        this.concern.assignResponse(response);
    }

    // 비밀번호 검증도 Aggregate Root를 통해서
    public boolean matchPassword(String rawPassword) {
        return this.concern.matchPassword(rawPassword);
    }

    // 응답이 할당되었는지 검증
    public boolean isComplete() {
        return concern != null && concern.getResponse() != null;
    }
}
