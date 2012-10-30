package pandora.avro.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class AvroUtil {
	public static <T> byte[] encode(T obj, Class<T> clazz, boolean isCompress)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DatumWriter<T> writer = new SpecificDatumWriter<T>(clazz);

		OutputStream output = null;
		Encoder encoder = null;

		if (isCompress == true) {
			output = new GZIPOutputStream(baos);
		} else {
			output = baos;
		}

		encoder = EncoderFactory.get().binaryEncoder(output, null);
		writer.write((T) obj, encoder);
		encoder.flush();

		output.close();

		return baos.toByteArray();
	}

	public static <T> T decode(byte[] in, Class<T> clazz, boolean isCompress)
			throws IOException {
		InputStream is = null;

		if (isCompress == true) {
			is = new GZIPInputStream(new ByteArrayInputStream(in));
		} else {
			is = new ByteArrayInputStream(in);
		}

		DatumReader<T> reader = new SpecificDatumReader<T>(clazz);

		Decoder decoder = DecoderFactory.get().binaryDecoder(is, null);
		T result = reader.read(null, decoder);

		is.close();

		return result;
	}
}
