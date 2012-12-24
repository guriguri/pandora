/*
 * Copyright guriguri(guriguri.kr@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pandora.disruptor;

import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandora.domain.User;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorTest {
	private static Logger log = LoggerFactory.getLogger(DisruptorTest.class);

	private static final int RING_SIZE = 1024;

	@SuppressWarnings({ "unchecked" })
	private Disruptor<DisruptorEvent> initDisruptor() {
		Disruptor<DisruptorEvent> disruptor = new Disruptor<DisruptorEvent>(
				DisruptorEvent.EVENT_FACTORY, Executors.newCachedThreadPool(),
				new SingleThreadedClaimStrategy(RING_SIZE),
				new SleepingWaitStrategy());

		NextStepService nextStepService = new NextStepService();

		DisruptorEventHandler disruptorEventHandler = new DisruptorEventHandler(
				nextStepService);
		disruptor.handleEventsWith(disruptorEventHandler);

		return disruptor;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void disruptor() throws InterruptedException {
		Disruptor<DisruptorEvent> disruptor = initDisruptor();
		disruptor.start();

		DisruptorPublisher disruptorPublisher = new DisruptorPublisher();
		for (int i = 0; i < 1000; i++) {
			User user = new User();
			user.setId(i);
			user.setName("name_" + i);

			disruptorPublisher.setValue(user);
			disruptor.publishEvent((EventTranslator) disruptorPublisher);

			log.debug("{}", user);
		}

		Thread.sleep(60000);
	}
}
