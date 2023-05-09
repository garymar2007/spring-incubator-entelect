package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.dto.Customer;
import entelect.training.incubator.spring.booking.dto.Flight;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Data
public class RestServiceHub {
    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * The following should be in properties files besides, username and password should be
     * replaced by a JWT token.
     */
    private final String USERNAME = "admin";
    private final String PASSWORD = "garymar";
    private final String CUSTOMER_URI = "http://localhost:8201/customers";

    private final String FLIGHT_URI = "http://localhost:8202/flights";

    public Customer getCustomerByCustomerId(String customerId) {
        return webClientBuilder.build().get().uri(CUSTOMER_URI + "/" + customerId)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .retrieve().bodyToMono(Customer.class).block();
    }

    public Flight getFlightByFlightId(String flightId) {
        return webClientBuilder.build().get().uri(FLIGHT_URI + "/" + flightId)
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .retrieve().bodyToMono(Flight.class).block();
    }
}
