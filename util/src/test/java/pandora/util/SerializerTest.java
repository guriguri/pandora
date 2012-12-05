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

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializerTest {
	private static Logger log = LoggerFactory
			.getLogger(SerializerTest.class);

	@Test
	public void test() throws Exception {
		int maxCount = 100000;
		Serial org = new Serial();
		org.setByteData(new byte[10]);
		org.setStringData("org");

		/*
		 * encode
		 */
		byte[] encoded = null;
		long start = System.nanoTime();
		for (int i = 0; i < maxCount; i++) {
			encoded = Serializer.encode(org);
		}
		log.debug("Java Encode, encode.size={}", encoded.length);
		log.debug("Java Encode, elapsedTime={}ns", (System.nanoTime() - start));

		/*
		 * decode
		 */
		Serial decoded = null;
		start = System.nanoTime();
		for (int i = 0; i < maxCount; i++) {
			decoded = (Serial) Serializer.decode(encoded);
		}
		log.debug("Java Decode, elapsedTime={}ns", (System.nanoTime() - start));

		Assert.assertEquals(org.getBooleanData(), decoded.getBooleanData());
		Assert.assertEquals(org.getDoubleData(), decoded.getDoubleData());
		Assert.assertEquals(org.getFloatData(), decoded.getFloatData());
		Assert.assertEquals(org.getIntData(), decoded.getIntData());
		Assert.assertEquals(org.getLongData(), decoded.getLongData());
		Assert.assertEquals(org.getStringData(), decoded.getStringData());
		Assert.assertEquals(new String(org.getByteData()),
				new String(decoded.getByteData()));
	}
}
