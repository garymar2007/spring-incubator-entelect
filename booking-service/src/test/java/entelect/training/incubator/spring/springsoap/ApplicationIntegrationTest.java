package entelect.training.incubator.spring.springsoap;

import entelect.training.incubator.spring.booking.config.LoyaltyClientConfig;
import entelect.training.incubator.spring.loyalty.ws.model.CaptureRewardsRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.ClassUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.client.core.WebServiceTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= LoyaltyClientConfig.class, loader= AnnotationConfigContextLoader.class)
@SpringBootTest
public class ApplicationIntegrationTest {
    private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    @LocalServerPort
    private int port = 0;

    @Before
    public void init() throws Exception {
        marshaller.setPackagesToScan(ClassUtils.getPackageName(CaptureRewardsRequest.class));
        marshaller.afterPropertiesSet();
    }

    @Test
    public void whenSendRequest_thenResponseIsNotNull() {
        WebServiceTemplate ws = new WebServiceTemplate(marshaller);
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setPassportNumber("12345");

        assertThat(ws.marshalSendAndReceive("http://locahost:" + port + "/ws", request)).isNotNull();
    }
}
