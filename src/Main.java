public class Main {
	public static void main(String[] args) {

		ARFFReader reader = new ARFFReader();
		reader.read();

		TreeLearner learner = new TreeLearner(reader.getExamples(),
				reader.getAttributes());
		DecisionTree tree = learner.learn();

		tree.print();
		learner.prune(tree);
		
	}
}
