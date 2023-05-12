package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.dto.Customer;
import entelect.training.incubator.spring.booking.dto.Flight;
import entelect.training.incubator.spring.booking.dto.Notification;
import entelect.training.incubator.spring.booking.jmsproducer.Producer;
import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingsSearchRequest;
import entelect.training.incubator.spring.booking.model.SearchType;
import entelect.training.incubator.spring.booking.repository.BookingsRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class BookingsService {

    @Autowired
    private final BookingsRepository bookingsRepository;

    @Autowired
    private final RestServiceHub restServiceHub;

    @Autowired
    private final LoyaltyClient loyaltyClient;

    @Autowired
    private final Producer producer;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Booking createBooking(Booking booking) throws NotFoundException {
        final String customerId = booking.getCustomerId();
        Customer customer = restServiceHub.getCustomerByCustomerId(customerId);
        if(customer == null || customer.getId() == null) {
            throw new NotFoundException("Save failed due to Customer ID not found");
        }
        final String flightId = booking.getFlightId();
        Flight flight = restServiceHub.getFlightByFlightId(flightId);
        if(flight == null || flight.getId() == null) {
            throw new NotFoundException("Save failed due to Flight ID not found");
        }
        final String referenceNumber = UUID.randomUUID().toString().substring(0,10);
        booking.setReferenceNumber(referenceNumber);
        Booking successfulBooking = bookingsRepository.save(booking);
        //Invoking SOAP WS to capture rewards
        loyaltyClient.captureRewards(customer.getPassportNumber(), new BigDecimal(1));

        //Send SMS to the user.
        final String message = "Molo Air: Confirming flight" + flightId + " booked for "
                + customer.getFirstName() + " " + customer.getLastName() + " on " + flight.getDepartureTime();
        producer.sendMessage(new Notification(customer.getPhoneNumber(), message));
        return successfulBooking;
    }

    public List<Booking> getBookings() {
        Iterable<Booking> bookingIterable = bookingsRepository.findAll();

        List<Booking> result = new ArrayList<>();
        bookingIterable.forEach(result::add);

        return result;
    }

    public Booking getBooking(Integer id) {
        Optional<Booking> bookingOptional = bookingsRepository.findById(id);
        return bookingOptional.orElse(null);
    }

    public Booking searchBookings(BookingsSearchRequest searchRequest) {
        if(searchRequest.getCustomerId() != null) {
            searchRequest.setSearchType(SearchType.CUSTOMER_ID_SEARCH);
        } else if(searchRequest.getReferenceNumber() != null) {
            searchRequest.setSearchType(SearchType.REFERENCE_NUMBER_SEARCH);
        }

        Map<SearchType, Supplier<Optional<Booking>>> searchStrategies = new HashMap<>();

        searchStrategies.put(SearchType.CUSTOMER_ID_SEARCH, () -> bookingsRepository.findByCustomerId(searchRequest.getCustomerId()));
        searchStrategies.put(SearchType.REFERENCE_NUMBER_SEARCH, () -> bookingsRepository.findByReferenceNumber(searchRequest.getReferenceNumber()));


        Optional<Booking> bookingOptional = searchStrategies.get(searchRequest.getSearchType()).get();

        return bookingOptional.orElse(null);
    }
}
