package entelect.training.incubator.spring.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class BookingsServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(BookingsServiceApplication.class, args);
    }
}
