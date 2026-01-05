package TripToN.TripToN.service.responseService;

import TripToN.TripToN.domain.Concern;

@FunctionalInterface
public interface ResponseService {
    public String response(Concern concern);
}
