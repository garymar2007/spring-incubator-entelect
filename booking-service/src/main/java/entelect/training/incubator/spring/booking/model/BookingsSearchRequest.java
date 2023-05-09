package entelect.training.incubator.spring.booking.model;

import lombok.Data;

@Data
public class BookingsSearchRequest {
    private String customerId;
    private String referenceNumber;

    private SearchType searchType;
}
