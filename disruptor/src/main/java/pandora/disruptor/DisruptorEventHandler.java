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

import com.lmax.disruptor.EventHandler;

public class DisruptorEventHandler implements EventHandler<DisruptorEvent> {
	private NextStepService nextServiceService;

	public DisruptorEventHandler(NextStepService nextServiceService) {
		this.nextServiceService = nextServiceService;
	}

	@Override
	public void onEvent(final DisruptorEvent event, final long sequence,
			final boolean endOfBatch) throws Exception {
		nextServiceService.nextStep(event.getValue());
	}
}
