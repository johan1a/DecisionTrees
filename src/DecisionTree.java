import java.util.HashMap;

public class DecisionTree {
	Node root;

	public DecisionTree(Attribute attribute) {
		root = new Node(attribute);
	}

	public DecisionTree(String output) {
		root = new Node(output);
	}

	public void addBranch(String v, DecisionTree subTree) {
		root.addChild(v, subTree);
	}

	public Node getRoot() {
		return root;
	}

	class Node {
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

		private boolean isOutputNode() {
			return output != null;
		}
	}

	public void print() {
		root.print("");

	}

}
