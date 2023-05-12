package entelect.training.incubator.spring.booking.jmsproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.booking.dto.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Map;

@Component
public class Producer {
    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    Queue queue;

    public void sendMessage(Notification notification) {
        System.out.println("Sending message " + notification.getMessage()
                + " to phone number: " + notification.getPhoneNumber() + " via queue - " + queue);
        try{
            ObjectMapper mapper = new ObjectMapper();
            String notificationAsJson = mapper.writeValueAsString(notification);

            jmsTemplate.convertAndSend(queue, notificationAsJson);
            System.out.println("Publish message successfully");
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
