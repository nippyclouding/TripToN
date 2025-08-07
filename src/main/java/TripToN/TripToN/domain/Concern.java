package TripToN.TripToN.domain;

import TripToN.TripToN.domain.luggages.Luggage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class Concern {
    @NotBlank @Length(max = 20)
    private String userName;
    @NotBlank
    @Length(max = 100)
    private String concern;
    @NotBlank @Length(max = 4)
    private String password;
    private Luggage luggage;
}
