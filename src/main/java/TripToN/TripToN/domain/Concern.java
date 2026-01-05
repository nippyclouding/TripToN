package TripToN.TripToN.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Embeddable
@Getter @Setter @EqualsAndHashCode
public class Concern {

    @NotBlank @Length(max = 20)
    private String userName;

    @NotBlank @Length(max = 4)
    private String password;

    @NotBlank @Length(max = 120)
    @Column(columnDefinition = "TEXT")
    private String concern;

    @Column(columnDefinition = "TEXT")
    private String response;

    //JPA 전용 생성자
    protected   Concern() {
    }

    public Concern(String userName, String concern, String password) {
        this.userName = userName;
        this.concern = concern;
        this.password = password;
    }


}
