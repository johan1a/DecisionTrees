public class Main {
	public static void main(String[] args) {
		ARFFReader reader = new ARFFReader();
		reader.read();
		TreeLearner learner = new TreeLearner(reader.getExamples(),
				reader.getAttributes());
		DecisionTree tree = learner.learn();

		Util.saveToFile("tree", tree.toString());
		learner.prune(tree);
		Util.saveToFile("treePruned", tree.toString());
		learner.prune(tree);
		Util.saveToFile("treePruned2", tree.toString());
		/*
		 * TODO trädet försvinner efter cirka 8 iterationer... ska man ens
		 * iterera?
		 */
	}
}
