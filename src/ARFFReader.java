import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ARFFReader {
	ArrayList<Attribute> attributes;
	ArrayList<Example> examples;
	private final String arffPath = "ARFF/";

	public void read() {
		attributes = new ArrayList<Attribute>();
		examples = new ArrayList<Example>();

		String fileName;
		fileName = "restaurants.ARFF";
		//fileName = "weather.nominal.arff";
		try {
			BufferedReader br = new BufferedReader(new FileReader(arffPath
					+ fileName));

			String line = br.readLine();
			while (!line.startsWith("@relation") || line.startsWith("%")) {
				line = br.readLine();
			}

			readAttributes(attributes, br);
			readExamples(examples, attributes, br);

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public ArrayList<Example> getExamples() {
		return examples;
	}

	private static void readExamples(ArrayList<Example> examples,
			ArrayList<Attribute> attributes, BufferedReader br) {
		String line;
		try {
			while (br.ready()) {
				line = br.readLine();
				if (!line.startsWith("%")) {
					String[] values = line.split(",");
					ArrayList<String> a = new ArrayList<String>();

					for (String s : values) {
						a.add(s);
					}
					examples.add(new Example(attributes, a));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readAttributes(ArrayList<Attribute> attributes,
			BufferedReader br) throws IOException {
		String line;
		while (br.ready()) {
			line = br.readLine();
			if (line.startsWith("%")) {
				continue;
			}
			if (line.startsWith("@data")) {
				break;
			}
			if (line.startsWith("@attribute")) {
				line = line.replace("{", "");
				line = line.replace("}", "");

				String[] temp = line.split(" ", 3);
				String temp2 = temp[2];
				String name = temp[1];

				String[] options = temp2.split(", ");
				ArrayList<String> attrOptions = new ArrayList<String>();
				for (String s : options) {
					attrOptions.add(s);
				}
				attributes.add(new Attribute(name, attrOptions));
			}
		}
	}
}
