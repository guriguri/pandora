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
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetSocket;

public class VertxTcpServerTestSkip {
	public static final int PORT = 12340;
	public static final String DOMAIN = "localhost";

	private class VertxServerEventHandler extends VertxEventHandler {
		public void handle(final NetSocket sock) {
			super.handle(sock);

			/*
			 * receive
			 */
			sock.dataHandler(new Handler<Buffer>() {
				@Override
				public void handle(Buffer buffer) {
					log.debug("dataHandler, sock={}, recvCnt={}, data={}",
							new Object[] { sock.writeHandlerID, ++recvCnt,
									buffer });
					send(sock, buffer);
//					sock.close();
				}
			});
		}
	}

	@Test
	public void test() {
		VertxTcpServer server = new VertxTcpServer();

		server.setDomain(DOMAIN);
		server.setPort(PORT);
		server.setHandler(new VertxServerEventHandler());

		new Thread(server).run();
	}

}
