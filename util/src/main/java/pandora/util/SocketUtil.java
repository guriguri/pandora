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
package pandora.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketUtil {
	private static final int BUFF_SIZE = 4096;

	public static byte[] getSocketToBytes(String ip, int port, String packet,
			int timeout) throws Exception {
		Socket socket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		byte buff[] = new byte[BUFF_SIZE];

		try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(ip, port);
			socket.connect(socketAddress, timeout);
			socket.setSoTimeout(timeout);

			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());

			dos.writeBytes(packet);

			dos.flush();
			dis.read(buff);
		} finally {
			if (dos != null) {
				dos.close();
			}

			if (dis != null) {
				dis.close();
			}

			if (socket != null) {
				if (socket != null) {
					socket.close();
				}
			}
		}

		return buff;
	}

	public static String getSocketToString(String ip, int port, String packet,
			int timeout, String enc) throws Exception {
		byte[] recv = getSocketToBytes(ip, port, packet, timeout);
		String result = new String(recv, enc);

		return result;
	}

	public static String getSocketToString(String ip, int port, String packet,
			int timeout) throws Exception {
		return getSocketToString(ip, port, packet, timeout, "UTF-8");
	}
}
