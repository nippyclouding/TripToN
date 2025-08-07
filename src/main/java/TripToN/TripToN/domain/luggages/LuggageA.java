package TripToN.TripToN.domain.luggages;

import TripToN.TripToN.domain.response.ResponseA;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@DiscriminatorValue("A")
public class LuggageA extends Luggage {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RID_A")
    private ResponseA response;
    public LuggageA() {}
    public LuggageA(String userName, String concern, String password) {
        super(userName, concern, password);
    }

}
