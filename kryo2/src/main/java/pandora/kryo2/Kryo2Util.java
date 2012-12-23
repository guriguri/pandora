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
package pandora.kryo2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Kryo2Util {
	public static byte[] encode(Object obj, boolean isCompress)
			throws IOException {
		Kryo kryo = new Kryo();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = null;

		if (isCompress == true) {
			output = new Output(new GZIPOutputStream(baos));
		} else {
			output = new Output(baos);
		}

		kryo.writeObject(output, obj);

		output.close();

		return baos.toByteArray();
	}

	public static <T> T decode(byte[] encodeByte, Class<T> clazz,
			boolean isCompress) throws IOException {
		Kryo kryo = new Kryo();

		ByteArrayInputStream bais = new ByteArrayInputStream(encodeByte);
		Input input = null;

		if (isCompress == true) {
			input = new Input(new GZIPInputStream(bais));
		} else {
			input = new Input(bais);
		}

		T t = kryo.readObject(input, clazz);

		input.close();

		return t;
	}
}
