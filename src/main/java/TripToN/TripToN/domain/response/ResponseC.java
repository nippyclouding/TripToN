package TripToN.TripToN.domain.response;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Entity
@DiscriminatorValue("C")
public class ResponseC extends Response {

    @ElementCollection
    @CollectionTable(name = "RID_C")
    private List<String> responses = new ArrayList<>();

    public ResponseC(List<String> responses) {
        this.responses = responses;
        String response1 = "This is luggage C's response1.";
        String response2 = "This is luggage C's response2.";
        String response3 = "This is luggage C's response3.";
        String response4 = "This is luggage C's response4.";

        responses.add(response1);
        responses.add(response2);
        responses.add(response3);
        responses.add(response4);
    }

    public ResponseC() {
        String response1 = "This is luggage C's response1.";
        String response2 = "This is luggage C's response2.";
        String response3 = "This is luggage C's response3.";
        String response4 = "This is luggage C's response4.";

        responses.add(response1);
        responses.add(response2);
        responses.add(response3);
        responses.add(response4);
    }
}
