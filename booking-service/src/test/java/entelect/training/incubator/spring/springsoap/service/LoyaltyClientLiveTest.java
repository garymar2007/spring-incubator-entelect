package entelect.training.incubator.spring.springsoap.service;

import entelect.training.incubator.spring.booking.config.LoyaltyClientConfig;
import entelect.training.incubator.spring.booking.service.LoyaltyClient;
import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsResponse;
import entelect.training.incubator.spring.loyalty.ws.model.RewardsBalanceResponse;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes= LoyaltyClientConfig.class, loader= AnnotationConfigContextLoader.class)
public class LoyaltyClientLiveTest {
    @Autowired
    LoyaltyClient client;

    @Test
    public void givenSoapService_whenPassportAmountGiven_then(){
        BigDecimal currentBalance = client.rewardsBalance("12345").getBalance();
        CaptureRewardsResponse response = client.captureRewards("12345", new BigDecimal(1));
        BigDecimal newBalance = currentBalance.add(new BigDecimal(1));
        assertEquals(newBalance, response.getBalance());
    }
}
