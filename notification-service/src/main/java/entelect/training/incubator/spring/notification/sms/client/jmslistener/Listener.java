package entelect.training.incubator.spring.notification.sms.client.jmslistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import entelect.training.incubator.spring.notification.model.Notification;
import entelect.training.incubator.spring.notification.sms.client.impl.MoloCellSmsClient;
import org.apache.activemq.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.ObjectMessage;

@Component
public class Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    @Autowired
    private MoloCellSmsClient client;

    @JmsListener(destination = "inbound.queue")
    public void receiveMessage(final String message) {
        try{
            LOGGER.info("Message received! {}", message);
            System.out.println("Received message " + message);
            ObjectMapper mapper = new ObjectMapper();
            Notification notification = mapper.readValue(message, Notification.class);
            client.sendSms(notification.getPhoneNumber(), notification.getMessage());
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }
}
