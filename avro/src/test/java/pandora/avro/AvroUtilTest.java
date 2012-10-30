package pandora.avro;

import java.nio.ByteBuffer;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pandora.avro.util.AvroUtil;
import pandora.java.PandoraRecord4Serializable;
import pandora.util.JavaObjectTransformer;

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
				JavaObjectTransformer.encode(java).length);
	}
}
