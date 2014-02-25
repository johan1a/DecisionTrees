public class Main {
	public static void main(String[] args) {
		ARFFReader reader = new ARFFReader();
		reader.read();
		TreeLearner learner = new TreeLearner(reader.getExamples(),
				reader.getAttributes());
		DecisionTree tree = learner.learn();

		Util.saveToFile("tree", tree.toString());
		learner.prune(tree);
		int k = 1;

		while (learner.couldPrune()) {
			Util.saveToFile("treePruned" + k, tree.toString());
			learner.prune(tree);
			k++;
		}
		System.out.println("Pruned " + (k - 1) + " times");
	}
}
