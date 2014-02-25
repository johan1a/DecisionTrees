import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
	public static void saveToFile(String fileName, String data) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(data);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
