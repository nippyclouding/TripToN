package TripToN.TripToN.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Luggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LID;

    @Embedded
    private Concern concern;

    private LocalDateTime dateTime;


    @Enumerated(EnumType.STRING)
    private LuggageType luggageType;

    //최종 생성
    public Luggage(Concern concern, LuggageType luggageType) {
        this.concern = concern;
        this.luggageType = luggageType;
        dateTime = LocalDateTime.now();
    }

    //JPA 전용 기본 생성자
    protected Luggage() {
    }

    // concern 내부가 null인지 검증
    // 서비스 계층에서 사용자 입력을 토대로 Luggage 객체를 생성하고 DB에 넣을 때 검증하는 메서드
    public boolean isComplete() {
        return concern != null &&
                concern.getResponse() != null;

    }
}
