package TripToN.TripToN.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Embeddable
@Getter @EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    Concern(String userName, String concern, String password) {
        this.userName = userName;
        this.concern = concern;
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public void assignResponse(String response) {
        if (response == null || response.isBlank()) {
            throw new IllegalArgumentException("응답이 비어있습니다");
        }
        this.response = response;
    }

    public boolean matchPassword(String rawPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, this.password);
    }
}
