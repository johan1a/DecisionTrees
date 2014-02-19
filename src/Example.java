import java.util.ArrayList;

public class Example {
	ArrayList<String> values;
	ArrayList<Attribute> attributeOrder;

	public Example(ArrayList<Attribute> attributeOrder, ArrayList<String> values) {
		this.values = values;
		this.attributeOrder = attributeOrder;
	}

	@Override
	public String toString() {
		return "Example [values=" + values + "]";
	}

	public String getClassification(Attribute a) {
		return values.get(attributeOrder.indexOf(a));
	}

	public String getOutput() {
		return values.get(values.size() - 1);
	}
}
