package TripToN.TripToN.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Embeddable
@Getter @Setter @EqualsAndHashCode
public class Concern {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @NotBlank @Length(max = 20)
    private String userName;

    @NotBlank
    @Column(length = 60)
    private String password;

    @NotBlank @Length(max = 120)
    @Column(columnDefinition = "TEXT")
    private String concern;

    @Column(columnDefinition = "TEXT")
    private String response;

    //JPA 전용 생성자
    protected Concern() {
    }

    public Concern(String userName, String concern, String password) {
        this.userName = userName;
        this.concern = concern;
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public boolean matchPassword(String rawPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, this.password);
    }
}
