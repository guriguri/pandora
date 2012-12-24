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

import pandora.domain.User;

import com.lmax.disruptor.EventTranslator;

public class DisruptorPublisher implements EventTranslator<DisruptorEvent> {
	private User value;

	public void setValue(User value) {
		this.value = value;
	}

	@Override
	public void translateTo(DisruptorEvent event, long sequence) {
		event.setValue(value);
	}
}
