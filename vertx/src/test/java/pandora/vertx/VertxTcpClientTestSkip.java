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
import org.vertx.java.core.net.NetSocket;

import pandora.util.SocketUtil;

public class VertxTcpClientTestSkip {
	private static final int PACKET_SIZE = 100;
	private static final int MAX_SEND_COUNT = 100000;
	private static final long THREAD_SLEEP_MSEC = 1000L;

	private static final float MILI_SEC = 1000.0F;
	private static final float NANO_SEC = 1000000000.0F;

	private class VertxClientEventHandler extends VertxEventHandler {
		public void connectHandler(final NetSocket sock) {
			setConnectNsec(System.nanoTime());

			for (int i = 0; i < MAX_SEND_COUNT; i++) {
				String packet = StringUtils.leftPad(String.valueOf(i),
						PACKET_SIZE, "0");
				send(sock, new Buffer(packet));
			}

			sock.close();
		}

		public void handle(final NetSocket sock) {
			super.handle(sock);
			connectHandler(sock);
		}
	}

	// @Test
	public final void asyncPerf() {
		VertxTcpClient client = new VertxTcpClient();
		VertxClientEventHandler handler = new VertxClientEventHandler();

		client.setDomain(VertxTcpServerTestSkip.DOMAIN);
		client.setPort(VertxTcpServerTestSkip.PORT);
		client.setHandler(handler);

		client.start();

		try {
			Thread.sleep(THREAD_SLEEP_MSEC);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		float totElapsedNsec = ((handler.getCloseNsec() - handler
				.getConnectNsec()) / NANO_SEC);
		float avgElapsedNsec = totElapsedNsec / MAX_SEND_COUNT * MILI_SEC;
		System.out.println("async, elapsedTime, total=" + totElapsedNsec
				+ "sec, avg=" + avgElapsedNsec + "msec");
	}

	@Test
	public final void syncPerf() {
		int errorCnt = 0;
		long start = System.nanoTime();
		String packet = StringUtils.leftPad(String.valueOf(99999), PACKET_SIZE,
				"0");
		for (int i = 0; i < MAX_SEND_COUNT; i++) {
			try {
				SocketUtil.getSocketToString(VertxTcpServerTestSkip.DOMAIN,
						VertxTcpServerTestSkip.PORT, packet, 100);
			} catch (Exception e) {
				e.printStackTrace();
				errorCnt++;
			}
		}

		float totElapsedNsec = (System.nanoTime() - start);
		float avgElapsedNsec = totElapsedNsec / MAX_SEND_COUNT;
		System.out.println("sync, elapsedTime, total="
				+ (totElapsedNsec / NANO_SEC) + "sec, avg="
				+ (avgElapsedNsec / MILI_SEC) + "msec, tps="
				+ (MAX_SEND_COUNT / (totElapsedNsec / NANO_SEC))
				+ ", errorCnt=" + errorCnt + ", errorRate="
				+ (errorCnt * 100.0F / MAX_SEND_COUNT));
	}
}
