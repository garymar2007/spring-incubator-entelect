package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsRequest;
import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsResponse;
import entelect.training.incubator.spring.loyalty.ws.model.RewardsBalanceRequest;
import entelect.training.incubator.spring.loyalty.ws.model.RewardsBalanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;

@Service
public class LoyaltyClient extends WebServiceGatewaySupport {

    private static final Logger logger = LoggerFactory.getLogger(LoyaltyClient.class);
    public CaptureRewardsResponse captureRewards(String passport, BigDecimal amount) {
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setPassportNumber(passport);
        request.setAmount(amount);

        CaptureRewardsResponse response = (CaptureRewardsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }

    public RewardsBalanceResponse rewardsBalance(String passport) {
        RewardsBalanceRequest request = new RewardsBalanceRequest();
        request.setPassportNumber(passport);

        RewardsBalanceResponse response = (RewardsBalanceResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        return response;
    }
}
