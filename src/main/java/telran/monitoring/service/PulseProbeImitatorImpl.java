package telran.monitoring.service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import telran.monitoring.model.PulseProbe;

@Service
public class PulseProbeImitatorImpl implements PulseProbeImitator {
	
	@Value("${app.patients.amount:50}")
	int patientsAmount = 50;
	@Value("${app.jump.probability:70}")
	int jumpProbability = 70;
	@Value("${app.increase.probability:50}")
	int increaseProbability = 50;
	@Value("${app.jump.multiplier:15}")
	int jumpMultiplier = 15;
	@Value("${app.no-jump.multiplier:5}")
	int noJumpMultiplier = 5;
	@Value("${app.pulse.value.min:60}")
	int minValue = 60;
	@Value("${app.pulse.value.max:120}")
	int maxValue = 120;
	static int seqNumber = 0;
	
	private static Logger LOG = LoggerFactory.getLogger(PulseProbeImitatorImpl.class);
	//@Autowired
	Map<Long, PulseProbe> pulseProbes = new HashMap<>();
	
	@Override
	public PulseProbe nextProbe() {
		seqNumber++;
		long id = getRandom(1, patientsAmount + 1);
		PulseProbe res = new PulseProbe();
		res = pulseProbes.get(id);
		int newValue = 0;
		if(res != null) {
			LOG.info("modifying pulseProb: {}", res.toString());
			int value = res.value;
			newValue = modifyPulseProb(value);
		} 
		else {
			newValue = (int) getRandom(minValue, maxValue);
			res = new PulseProbe();
			res.patientId = id;
			LOG.info("new pulseProb: {}", res.toString());
		}
		res.value = newValue;
		res.timestamp = getTimeStamp();
		res.sequenceNumber = seqNumber;
		pulseProbes.put(id, res);
		return res;
		
	}


	private int modifyPulseProb(int value) {
		int res = 0;
		if(getRandom(0, 100) <= jumpProbability) {
			if(getRandom(0, 100) <= increaseProbability) {
				res = value + jumpMultiplier;
			} else {
				res = value - jumpMultiplier;
			}
		} else {
			if(getRandom(0, 100) <= increaseProbability) {
				res = value + noJumpMultiplier;
			} else {
				res = value - noJumpMultiplier;
			}
		}
		return res;
	}


	private long getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.getTime();
	}

	private long getRandom(int min, int max) {
		
		return ThreadLocalRandom.current().nextInt(min, max);
	}

}
