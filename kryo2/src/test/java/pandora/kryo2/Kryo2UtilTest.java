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

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandora.domain.User;
import pandora.util.Serializer;

public class Kryo2UtilTest {
	private static Logger log = LoggerFactory.getLogger(Kryo2UtilTest.class);

	@Test
	public void test() throws Exception {
		int maxCount = 100000;
		int seed = (int) (System.currentTimeMillis() / 1000);
		User org = new User();
		org.setId(seed);
		org.setName("name_" + seed);

		for (boolean isCompress : new boolean[] { true, false }) {
			/*
			 * encode
			 */
			byte[] encoded = null;
			long start = System.nanoTime();
			for (int i = 0; i < maxCount; i++) {
				encoded = Kryo2Util.encode(org, isCompress);
			}
			log.debug("Kryo2 Encode, isCompress={}, encode.size={}",
					isCompress, encoded.length);
			log.debug("Kryo2 Encode, isCompress={}, elapsedTime={}ns",
					isCompress, (System.nanoTime() - start));

			/*
			 * decode
			 */
			User decoded = null;
			start = System.nanoTime();
			for (int i = 0; i < maxCount; i++) {
				decoded = Kryo2Util.decode(encoded, User.class, isCompress);
			}

			log.debug("Kryo2 Decode, isCompress={}, elapsedTime={}ns",
					isCompress, (System.nanoTime() - start));

			Assert.assertEquals("not equal id", org.getId(), decoded.getId());
			Assert.assertEquals("not equal name", org.getName(),
					decoded.getName());
		} // end of for (boolean isCompress : new boolean[] { true, false })

		log.debug("Java Encode, encode.size={}", Serializer.encode(org).length);
	}
}
