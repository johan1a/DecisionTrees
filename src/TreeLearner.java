import java.util.ArrayList;
import java.util.HashMap;

public class TreeLearner {
	private EntropyMath entropyMath;
	private final ArrayList<Example> allExamples;
	private final ArrayList<Attribute> attributes;
	final String posOutput = "Yes", negOutput = "No";

	public TreeLearner(ArrayList<Example> allExamples,
			ArrayList<Attribute> attributes) {
		this.allExamples = allExamples;
		this.attributes = attributes;
		this.entropyMath = new EntropyMath(allExamples);
	}

	public DecisionTree learn() {
		return learn(allExamples, attributes);
	}

	private DecisionTree learn(ArrayList<Example> examples,
			ArrayList<Attribute> attributes) {
		if (examples.size() == 0) {
			return new DecisionTree(getPluralityValue(allExamples));
		}
		Attribute a = attributes.get(0);
		String oldClassification = examples.get(0).getClassification(a);
		String classification;
		boolean allSame = true;
		for (int i = 1; i < examples.size(); i++) {
			classification = examples.get(i).getClassification(a);
			if (classification != oldClassification) {
				allSame = false;
				break;
			}
		}
		if (allSame) {
			return new DecisionTree(examples.get(0).getOutput());
		}

		if (attributes.isEmpty()) {
			return new DecisionTree(getPluralityValue(examples));
		}

		Attribute attribute = importance(attributes);
		DecisionTree tree = new DecisionTree(attribute);
		for (String v : attribute.getOptions()) {
			ArrayList<Example> exs = new ArrayList<Example>();
			for (Example e : examples) {
				if (e.getClassification(attribute).equals(v)) {
					exs.add(e);
				}
			}

			ArrayList<Attribute> attributes2 = new ArrayList<Attribute>();
			attributes2.addAll(attributes);
			attributes2.remove(attribute);
			DecisionTree subTree = learn(exs, attributes2);
			tree.addBranch(v, subTree);
		}
		return tree;
	}

	private String getPluralityValue(final ArrayList<Example> examples) {
		int sum1 = 0, sum2 = 0;
		for (Example e : examples) {
			if (e.getOutput().equals(posOutput)) {
				sum1++;
			} else {
				sum2++;
			}
		}

		if (sum1 > sum2) {
			return posOutput;
		} else if (sum2 > sum1) {
			return negOutput;
		} else {
			if (Math.random() < 0.5) {
				return posOutput;
			}
			return negOutput;
		}
	}

	private Attribute importance(final ArrayList<Attribute> attributes) {
		double max = 0, gain = 0;
		Attribute result = null;

		for (Attribute attribute : attributes) {
			gain = entropyMath.calculateGain(attribute);
			if (gain >= max) {
				max = gain;
				result = attribute;
			}
		}
		return result;
	}

	public void prune(DecisionTree tree) {
		prune(tree.getRoot());
	}

	private void prune(Node node) {
		HashMap<String, Node> children = node.children;
		if (children != null) {
			boolean allChildrenAreLeaves = true;
			for (Node n : children.values()) {
				if (!n.isOutputNode()) {
					allChildrenAreLeaves = false;
					break;
				}
			}
			if (!allChildrenAreLeaves) {
				for (Node n : children.values()) {
					prune(n);
				}
			} else if (!entropyMath.relevant(node.attribute)) {
				node.setOutput(getPluralityValue(allExamples));
				node.setChildren(null);
			}
		}
	}
}
