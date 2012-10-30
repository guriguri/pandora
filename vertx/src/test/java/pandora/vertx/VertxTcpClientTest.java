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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.vertx.java.core.buffer.Buffer;

public class VertxTcpClientTest {
	private static final int PACKET_SIZE = 100;
	private static final int MAX_SEND_COUNT = 100000;
	private static final long THREAD_SLEEP_MSEC = 1000L;

	private static final float MILI_SEC = 1000.0F;
	private static final float NANO_SEC = 1000000000.0F;

	private class VertxClientEventHandler extends VertxEventHandler {
		public void connectHandler() {
			setConnectNsec(System.nanoTime());

			for (int i = 0; i < MAX_SEND_COUNT; i++) {
				String packet = StringUtils.leftPad(String.valueOf(i),
						PACKET_SIZE, "0");
				send(new Buffer(packet));
			}

			close();
		}
	}

	@Test
	public final void perf() {
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

		float totElapsedNsec = ((handler.getCloseNsec() - handler
				.getConnectNsec()) / NANO_SEC);
		float avgElapsedNsec = totElapsedNsec / MAX_SEND_COUNT * MILI_SEC;
		System.out.println("elapsedTime, total=" + totElapsedNsec + "sec, avg="
				+ avgElapsedNsec + "msec");
	}
}
