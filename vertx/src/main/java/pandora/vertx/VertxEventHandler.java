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

import java.nio.channels.ClosedChannelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.SimpleHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetSocket;

public class VertxEventHandler implements Handler<NetSocket> {
	private static Logger log = LoggerFactory
			.getLogger(VertxEventHandler.class);

	private NetSocket sock;

	private int recvCnt = 0;
	private int failCnt = 0;
	private int sendCnt = 0;
	private int closedChannelExceptionCnt = 0;

	private boolean isClose = false;

	private long connectNsec = 0L;
	private long closeNsec = 0L;

	public boolean isClose() {
		return isClose;
	}

	public void setClose(boolean isClose) {
		if (isClose == true) {
			setCloseNsec(System.nanoTime());
		}

		this.isClose = isClose;
	}

	public long getConnectNsec() {
		return connectNsec;
	}

	public void setConnectNsec(long connectNsec) {
		this.connectNsec = connectNsec;
	}

	public long getCloseNsec() {
		return closeNsec;
	}

	public void setCloseNsec(long closeNsec) {
		this.closeNsec = closeNsec;
	}

	public void close() {
		sock.drainHandler(new SimpleHandler() {
			public void handle() {
				sock.close();

				log.debug("close socket, sock={}", sock.writeHandlerID);
			}
		});
	}

	public void send(Buffer buffer) {
		if (isClose == true) {
			log.error("close socket, sock={}", sock.writeHandlerID);
		}
		else if (!sock.writeQueueFull()) {
			sock.write(buffer, new SimpleHandler() {
				public void handle() {
					log.debug("write socket, sock={}, sendCnt={}",
							sock.writeHandlerID, ++sendCnt);
				}
			});
		}
		else {
			sock.pause();
			log.debug("pause socket, sock={}", sock.writeHandlerID);

			sock.drainHandler(new SimpleHandler() {
				public void handle() {
					sock.resume();
					log.debug("drainHandler, resume socket, sock={}",
							sock.writeHandlerID);
				}
			});
		}
	}

	public void connectHandler() {
		log.debug("connectHandler, sock={}", sock.writeHandlerID);
	}

	public void handle(final NetSocket sock) {
		this.isClose = false;
		this.sock = sock;

		/*
		 * receive
		 */
		sock.dataHandler(new Handler<Buffer>() {
			@Override
			public void handle(Buffer buffer) {
				log.debug("dataHandler, sock={}, recvCnt={}, data={}",
						new Object[] { sock.writeHandlerID, ++recvCnt, buffer });
			}
		});

		sock.closedHandler(new Handler<Void>() {
			@Override
			public void handle(Void arg0) {
				setClose(true);
				log.debug("closedHandler, sock={}", sock.writeHandlerID);
			}
		});

		sock.endHandler(new Handler<Void>() {
			@Override
			public void handle(Void arg0) {
				log.debug("endHandler, sock={}", sock.writeHandlerID);
			}
		});

		sock.exceptionHandler(new Handler<Exception>() {
			@Override
			public void handle(Exception e) {
				log.error("exceptionHandler, sock={}, sendCnt={}, failCnt={}",
						new Object[] { sock.writeHandlerID, sendCnt, ++failCnt,
								e });
				if (e instanceof ClosedChannelException) {
					closedChannelExceptionCnt++;

					if (closedChannelExceptionCnt == 2) {
						setClose(true);
					}
				}
			}
		});

		/*
		 * connect
		 */
		connectHandler();
	}
}
