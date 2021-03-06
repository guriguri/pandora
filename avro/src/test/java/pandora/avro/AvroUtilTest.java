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
package pandora.avro;

import java.nio.ByteBuffer;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandora.avro.util.AvroUtil;
import pandora.domain.PandoraRecord4Serializable;
import pandora.domain.avro.PandoraRecord;
import pandora.util.Serializer;

public class AvroUtilTest {
	private static Logger log = LoggerFactory.getLogger(AvroUtilTest.class);

	@Test
	public void test() throws Exception {
		int maxCount = 100000;
		PandoraRecord org = new PandoraRecord();
		org.setByteData(ByteBuffer.allocate(10));
		org.setStringData("org");

		for (boolean isCompress : new boolean[] { true, false }) {
			/*
			 * encode
			 */
			byte[] encoded = null;
			long start = System.nanoTime();
			for (int i = 0; i < maxCount; i++) {
				encoded = AvroUtil.encode(org, PandoraRecord.class, isCompress);
			}
			log.debug("Avro Encode, isCompress={}, encode.size={}", isCompress,
					encoded.length);
			log.debug("Avro Encode, isCompress={}, elapsedTime={}ns",
					isCompress, (System.nanoTime() - start));

			/*
			 * decode
			 */
			PandoraRecord decoded = null;
			start = System.nanoTime();
			for (int i = 0; i < maxCount; i++) {
				decoded = AvroUtil.decode(encoded, PandoraRecord.class,
						isCompress);
			}

			log.debug("Avro Decode, isCompress={}, elapsedTime={}ns",
					isCompress, (System.nanoTime() - start));

			Assert.assertEquals(org, decoded);
		} // end of for (boolean isCompress : new boolean[] { true, false })

		PandoraRecord4Serializable java = new PandoraRecord4Serializable();
		java.setByteData(ByteBuffer.allocate(10));
		java.setStringData("org");
		log.debug("Java Encode, encode.size={}",
				Serializer.encode(java).length);
	}
}
