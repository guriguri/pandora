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
package pandora.vertx;

import org.junit.Test;
import org.vertx.java.core.buffer.Buffer;

public class VertxTcpClientTest {
	private static final int MAX_SEND_COUNT = 1000;
	private static final long THREAD_SLEEP_MSEC = 1000L;

	private class VertxClientEventHandler extends VertxEventHandler {
		private boolean isClose = false;

		public boolean isClose() {
			return isClose;
		}

		public void setClose(boolean isClose) {
			this.isClose = isClose;
		}

		public void connectHandler() {
			for (int i = 0; i < MAX_SEND_COUNT; i++) {
				String packet = String.format("%0100d", i);
				send(new Buffer(packet));
			}

			close();
			setClose(true);
		}
	}

	@Test
	public final void test() {
		VertxTcpClient client = new VertxTcpClient();
		VertxClientEventHandler handler = new VertxClientEventHandler();

		client.setDomain(VertxTcpServerTest.DOMAIN);
		client.setPort(VertxTcpServerTest.PORT);
		client.setHandler(handler);

		client.start();

		while (!handler.isClose()) {
			try {
				Thread.sleep(THREAD_SLEEP_MSEC);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
