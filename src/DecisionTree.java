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

	public void print() {
		root.print();
	}

	@Override
	public String toString() {
		return root.toString();
	}

	public void setRoot(Node root) {
		this.root = root;
	}
}
