package telran.monitoring;

import java.util.Scanner;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.PreDestroy;
import telran.monitoring.model.PulseProbe;
import telran.monitoring.service.PulseProbeImitator;

@SpringBootApplication
public class PulseProbesImitatorApplication {
	@Autowired
	PulseProbeImitator imitator;

	private static final Object SHUTDOWN = "shutdown";
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(PulseProbesImitatorApplication.class,  args);
	
		//Test
//		var test = ctx.getBean(PulseProbeImitator.class);
//        for(int i = 0; i < 50; i++) {
//        	test.nextProbe();
//        }
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.println("To stop server type " + SHUTDOWN);
			String line = scanner.nextLine();
			if (line.equals(SHUTDOWN)) {
				break;
			}
		}
		ctx.close();
	}
	
	@Bean
	Supplier<PulseProbe> pulseProbeSupplier(){
		return imitator::nextProbe;
	}
	
	
	
	@PreDestroy
	void preDestroy() {
		System.out.println("bye - shutdown has been performed");
	}

}