import java.util.ArrayList;
import java.util.HashMap;

public class TreeLearner {
	private EntropyMath entropyMath;
	private final ArrayList<Example> allExamples;
	private final ArrayList<Attribute> attributes;
	final String posOutput = "yes", negOutput = "no";
	private int nbrPos = 0, nbrNeg = 0;
	private boolean couldPrune = false; // True if the last prune attempt was
										// succesful

	public TreeLearner(ArrayList<Example> allExamples,
			ArrayList<Attribute> attributes) {
		this.allExamples = allExamples;
		this.attributes = attributes;
		this.entropyMath = new EntropyMath(allExamples);
	}

	/* Creates a decision tree using the ID3 algorithm */
	public DecisionTree learn() {
		return id3(allExamples, attributes);
	}

	private DecisionTree id3(ArrayList<Example> examples,
			ArrayList<Attribute> attributes) {
		if (examples.size() == 0) {
			return new DecisionTree(getPluralityValue(allExamples));
		}

		String oldClassification = examples.get(0).getOutput();
		String classification;
		boolean allSame = true;
		for (int i = 1; i < examples.size(); i++) {
			classification = examples.get(i).getOutput();
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
			DecisionTree subTree = id3(exs, attributes2);
			tree.addBranch(v, subTree);
		}
		return tree;
	}

	/* Returns the attribute with highest gain */
	private Attribute importance(final ArrayList<Attribute> attributes) {
		double max = Double.MIN_VALUE, gain = 0;
		Attribute result = attributes.get(0);

		for (Attribute attribute : attributes) {
			gain = entropyMath.calculateGain(attribute);
			if (gain >= max) {
				max = gain;
				result = attribute;
			}
		}
		return result;
	}

	/* Chi squared pruning */
	public void prune(DecisionTree tree) {
		couldPrune = false;
		calculateTreeNbrPosNeg(tree.getRoot());
		entropyMath.setTreeTotalPandN(nbrPos, nbrNeg);
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

				/*
				 * * A node with only leaves for children. Check if it can be
				 * pruned.
				 */
			} else if (!entropyMath.relevant(node)) {
				node.setOutput(getPluralityValue(node));
				node.setChildren(null);
				couldPrune = true;
			}
		}
	}

	/* Returns the most common output among the examples. */
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
			return (Math.random() < 0.5) ? posOutput : negOutput;
		}
	}

	/*
	 * Calculates and returns the most common output in the tree starting with
	 * the Node node, and puts the number of positive outputs in nbrPos and the
	 * number of negative output in nbrNeg.
	 */
	private String getPluralityValue(Node node) {
		nbrPos = 0;
		nbrNeg = 0;
		calculateTreeNbrPosNeg(node);
		if (nbrPos > nbrNeg) {
			return posOutput;
		} else if (nbrNeg > nbrPos) {
			return negOutput;
		}
		return (Math.random() < 0.5) ? posOutput : negOutput;
	}

	private void calculateTreeNbrPosNeg(Node node) {
		HashMap<String, Node> children = node.children;
		if (children != null) {
			for (Node n : children.values()) {
				calculateTreeNbrPosNeg(n);
			}
		} else {
			if (node.output.equals(posOutput)) {
				nbrPos++;
			} else if (node.output.equals(negOutput)) {
				nbrNeg++;
			} else {
				System.exit(1);
			}
		}
	}

	public boolean couldPrune() {
		return couldPrune;
	}
}
