package TripToN.TripToN.domain.luggages;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "luggage_type")
public abstract class Luggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LID;
    private String userName;
    @NotBlank @Length(max = 100)
    private String concern;
    @NotBlank @Length(max = 4)
    private String password;
    private LocalDateTime dateTime;
    private String answer;


    public Luggage(String userName, String concern, String password) {
        this.userName = userName;
        this.concern = concern;
        this.password = password;
        this.dateTime = LocalDateTime.now();
    }

    protected Luggage() {
    }
}
