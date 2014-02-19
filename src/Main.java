public class Main {
	public static void main(String[] args) {

		ARFFReader reader = new ARFFReader();
		reader.read();

		DecisionTree tree = TreeLearner.learn(reader.getExamples(),
				reader.getAttributes(), reader.getExamples());

		tree.print();
	}
}
