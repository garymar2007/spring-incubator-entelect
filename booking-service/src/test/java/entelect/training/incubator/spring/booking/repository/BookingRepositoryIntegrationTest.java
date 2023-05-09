package entelect.training.incubator.spring.booking.repository;

import entelect.training.incubator.spring.booking.model.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookingRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private entelect.training.incubator.spring.booking.repository.BookingsRepository bookingsRepository;

    @Test
    public void whenFindByCustomerId_thenReturnBooking() {
        Booking booking = createTestBooking("1", "1", "ABC123");
        entityManager.persistAndFlush(booking);

        Optional<Booking> found = bookingsRepository.findByCustomerId(booking.getCustomerId());
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerId()).isEqualTo(booking.getCustomerId());
    }

    @Test
    public void whenFindByReferenceId_thenReturnBooking() {
        Booking booking = createTestBooking("1", "1", "ABC123");
        entityManager.persistAndFlush(booking);

        Optional<Booking> found = bookingsRepository.findByReferenceNumber(booking.getReferenceNumber());
        assertThat(found).isPresent();
        assertThat(found.get().getReferenceNumber()).isEqualTo(booking.getReferenceNumber());
    }

    private Booking createTestBooking(String customerId, String flightId, String referenceNumber) {
        Booking booking = new Booking();
        booking.setFlightId(flightId);
        booking.setCustomerId(customerId);
        booking.setReferenceNumber(referenceNumber);
        return booking;
    }

}