package TripToN.TripToN.domain.luggages;


import TripToN.TripToN.domain.response.ResponseC;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@DiscriminatorValue("C")
public class LuggageC extends Luggage {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RID_C")
    private ResponseC response;
    public LuggageC() {
    }
    public LuggageC(String userName, String concern, String password) {
        super(userName, concern, password);
    }
}
