package entelect.training.incubator.spring.booking.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.booking.BookingsServiceApplication;
import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.model.BookingsSearchRequest;
import entelect.training.incubator.spring.booking.repository.BookingsRepository;
import entelect.training.incubator.spring.booking.service.BookingsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookingsServiceApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingsRestControllerIntegrationTest {

    private static final String TEST_BOOKING_CUCTOMER_ID = "1";
    private static final String TEST_BOOKING_FLIGHT_ID = "1";
    private static final String TEST_BOOKING_REFERENCE_NUMBER = "ABC123";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingsService mockBookingService;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private BookingsRepository repository;

    private final String USERNAME = "admin";
    private final String PASSWORD = "garymar";

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateBooking() throws Exception {
        Booking booking = new Booking();
        booking.setCustomerId("1");
        booking.setFlightId("1");
        booking.setReferenceNumber("ABC123");

        Mockito.when(mockBookingService.createBooking(booking)).thenReturn(booking);
        webClient.post()
                .uri("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(booking))
                .headers(headers -> headers.setBasicAuth(USERNAME, PASSWORD))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(mockBookingService, times(1)).createBooking(booking);
    }

    @Test
    public void givenBookings_whenGetBookingById_thenReturnBooking() throws Exception {
        createTestBooking(1);

        mvc.perform(get("/bookings/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void givenBooking_whenGetBookingByCustomerId_thenReturnBooking() throws Exception {
        createTestBooking();

        BookingsSearchRequest bookingsSearchRequest = new BookingsSearchRequest();
        bookingsSearchRequest.setCustomerId(TEST_BOOKING_CUCTOMER_ID);

        mvc.perform(post("/bookings/search").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(bookingsSearchRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(TEST_BOOKING_CUCTOMER_ID)));
    }

    @Test
    public void givenBooking_whenGetBookingByReferenceNumber_thenReturnBooking() throws Exception {
        createTestBooking();

        BookingsSearchRequest bookingsSearchRequest = new BookingsSearchRequest();
        bookingsSearchRequest.setReferenceNumber(TEST_BOOKING_REFERENCE_NUMBER);

        mvc.perform(post("/bookings/search").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(bookingsSearchRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referenceNumber", is(TEST_BOOKING_REFERENCE_NUMBER)));
    }

    private void createTestBooking() {
        createTestBooking(null);
    }

    private void createTestBooking(Integer id) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setCustomerId(TEST_BOOKING_CUCTOMER_ID);
        booking.setFlightId(TEST_BOOKING_FLIGHT_ID);
        booking.setReferenceNumber(TEST_BOOKING_REFERENCE_NUMBER);

        repository.save(booking);
    }

    private static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}