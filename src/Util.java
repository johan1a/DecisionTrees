import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
	private static final String OUTPUT_PATH = "output/";

	public static void saveToFile(String fileName, String data) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH
					+ fileName));
			bw.write(data);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
