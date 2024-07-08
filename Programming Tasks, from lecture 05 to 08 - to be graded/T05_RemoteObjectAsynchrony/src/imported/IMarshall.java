package imported;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public interface IMarshall extends Serializable {
	
	public default byte[] marshall() throws Exception {
		try (var bos = new ByteArrayOutputStream()) {
			try (var out = new ObjectOutputStream(bos)) {
				out.writeObject(this);
				out.flush();
				return bos.toByteArray();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshall(byte[] data) throws ClassNotFoundException, IOException {
		try (var bis = new ByteArrayInputStream(data)) {
			try (var in = new ObjectInputStream(bis)) {
				return (T) in.readObject();
			}
		}
	}

	private static byte[] toPrimitive(Byte[] bytesIn) {
		byte[] bytesOut = new byte[bytesIn.length];
		for (int i = 0; i < bytesIn.length; i++) {
			bytesOut[i] = bytesIn[i];
		}
		return bytesOut;
	}

	public static Byte[] toObjects(byte[] bytesIn) {
		Byte[] bytesOut = new Byte[bytesIn.length];
		Arrays.setAll(bytesOut, n -> bytesIn[n]);
		return bytesOut;
	}
}