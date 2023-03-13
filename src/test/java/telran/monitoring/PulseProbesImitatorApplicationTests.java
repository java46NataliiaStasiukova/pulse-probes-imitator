package telran.monitoring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.model.PulseProbe;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class PulseProbesImitatorApplicationTests {
	@Autowired
	OutputDestination consumer;

	@Test
	void contextLoads() {
	}
	
	@Test
	void nextProbeTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		for(int i = 0; i < 20; i++) {
			Message<byte[]> message = consumer.receive(2000, "pulseProbeSupplier-out-0");
			assertNotNull(message);
			System.out.println(mapper.readValue(message.getPayload(), PulseProbe.class));
		}
	}

}
