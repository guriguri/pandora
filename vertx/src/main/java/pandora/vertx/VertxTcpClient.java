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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.net.NetClient;
import org.vertx.java.deploy.Verticle;

public class VertxTcpClient extends Verticle {
	private static Logger log = LoggerFactory.getLogger(VertxTcpClient.class);

	private int port;
	private String domain;
	private VertxEventHandler handler;

	public void setPort(int port) {
		this.port = port;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setHandler(VertxEventHandler handler) {
		this.handler = handler;
	}

	public void start() {
		if ((port != 0) && StringUtils.isNotEmpty(domain) && (handler != null)) {
			vertx = Vertx.newVertx();
			NetClient client = vertx.createNetClient();
			client.connect(port, domain, handler);
			log.info("START, connecting, socket://{}:{}", domain, port);
		}
		else {
			log.error("port={}, domain={}, handler.isNull={}", new Object[] {
					port, domain, (handler == null) });
		}
	}
}
