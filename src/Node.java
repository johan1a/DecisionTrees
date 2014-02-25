import java.util.HashMap;

public class Node {
	Attribute attribute;
	HashMap<String, Node> children;
	String output;

	public Node(Attribute attribute) {
		this.attribute = attribute;
		children = new HashMap<String, Node>();
	}

	public Node(String output) {
		this.output = output;
	}

	public void addChild(String v, DecisionTree subTree) {
		children.put(v, subTree.getRoot());

	}

	public void print(String indent) {
		if (children != null) {
			for (String option : children.keySet()) {
				System.out.print(indent + attribute + " = " + option);
				Node child = children.get(option);

				if (child.isOutputNode()) {
					child.print("");
				} else {
					System.out.println();
					child.print(indent + " ");
				}
			}
		} else {
			System.out.println(": " + output);
		}
	}
	
	public String toString(){
		return toString("");
	}

	public String toString(String indent) {
		String result = "";
		if (children != null) {
			for (String option : children.keySet()) {
				result = result + indent + attribute + " = " + option;
				Node child = children.get(option);

				if (child.isOutputNode()) {
					result = result + child.toString("");
				} else {
					result = result + "\n" + child.toString(indent + " ");
				}
			}
		} else {
			result = result + ": " + output + "\n";
		}
		return result;
	}

	boolean isOutputNode() {
		return output != null;
	}
}