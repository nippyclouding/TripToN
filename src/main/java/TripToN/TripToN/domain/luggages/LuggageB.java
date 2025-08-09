package TripToN.TripToN.domain.luggages;

import TripToN.TripToN.domain.Color;
import TripToN.TripToN.domain.response.ResponseB;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@DiscriminatorValue("B")
public class LuggageB extends Luggage {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RID_B")
    private ResponseB response;
    @Enumerated(EnumType.STRING)
    private Color color;
    public LuggageB() {}
    public LuggageB(String userName, String concern, String password) {
        super(userName, concern, password);
    }
}
