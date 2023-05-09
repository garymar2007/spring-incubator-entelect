package entelect.training.incubator.spring.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {
    private String phoneNumber;
    private String message;
}
