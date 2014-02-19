import java.util.ArrayList;

public class TreeLearner {
	public static DecisionTree learn(ArrayList<Example> examples,
			ArrayList<Attribute> attributes, ArrayList<Example> parentExamples) {

		if (examples.size() == 0) {
			return pluralityValue(parentExamples);
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
			return new DecisionTree(examples.get(0).getOutput()); // wrooong
		}

		if (attributes.isEmpty()) {

			return pluralityValue(examples);
		}

		Attribute attribute = importance(attributes, examples);
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
			DecisionTree subTree = learn(exs, attributes2, examples);
			tree.addBranch(v, subTree);
		}
		return tree;
	}

	private static DecisionTree pluralityValue(ArrayList<Example> examples) {
		final String output1 = "Yes", output2 = "No"; // TODO hårdkodat
		int sum1 = 0, sum2 = 0;
		for (Example e : examples) {
			if (e.getOutput().equals(output1)) {
				sum1++;
			} else {
				sum2++;
			}
		}

		if (sum1 > sum2) { // TODO ranomly pls
			return new DecisionTree(output1);
		}
		return new DecisionTree(output2);
	}

	private static Attribute importance(ArrayList<Attribute> attributes,
			ArrayList<Example> examples) {
		return attributes.get(0);
	}
}
