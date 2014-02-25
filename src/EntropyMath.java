import java.util.ArrayList;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

public class EntropyMath {
	ArrayList<Example> allExamples;
	private final String posOutput = "yes";
	int subTreePos = 0, subTreeNeg = 0;
	int totalPos = 0, totalNeg = 0;
	private final double CONFIDENCE = 0.95;

	public EntropyMath(ArrayList<Example> allExamples) {
		this.allExamples = allExamples;
		totalPos = 0;
		totalNeg = 0;
		for (Example e : allExamples) {
			if (e.getOutput().equals(posOutput)) {
				totalPos++;
			} else {
				totalNeg++;
			}
		}
	}

	/*
	 * Calculates the number of positive and negative leaves in the tree and
	 * puts them in subTreePos and subTreeNeg
	 */
	private void calculateTotalPosNeg(Node node) {
		if (node.children != null) {
			for (Node n : node.children.values()) {
				calculateTotalPosNeg(n);
			}
		} else {
			if (node.output.equals(posOutput)) {
				subTreePos++;
			} else {
				subTreeNeg++;
			}
		}
	}

	/* Calculates the gain of the Attribute */
	public double calculateGain(Attribute attribute) {
		return totalEntropy() - remainder(attribute);
	}

	/* Returns the total entropy */
	private double totalEntropy() {
		return b(totalPos / ((double) totalPos + totalNeg));
	}

	/* Checks if delta >= confidence */
	public boolean relevant(Node node) {
		double delta = delta(node);
		Attribute attribute = node.attribute;
		int degreesOfFreedom = attribute.getOptions().size() - 1;
		ChiSquaredDistribution db = new ChiSquaredDistribution(degreesOfFreedom);

		System.out.println(attribute.toString());
		System.out.println("Delta: " + delta);
		System.out.println("Probability: " + db.cumulativeProbability(delta));
		System.out.println("Confidence: " + CONFIDENCE);
		System.out.println("p >= q: "
				+ (db.cumulativeProbability(delta) <= CONFIDENCE));
		System.out.println();

		return db.cumulativeProbability(delta) >= CONFIDENCE;
	}

	private double delta(Node node) {
		double delta = 0;
		int p = totalPos, n = totalNeg;

		subTreePos = 0;
		subTreeNeg = 0;
		calculateTotalPosNeg(node);
		double pk = subTreePos, nk = subTreeNeg;

		double pkhat = p * (pk + nk) / (p + n);
		double nkhat = n * (pk + nk) / (p + n);

		delta += Math.pow((pk - pkhat), 2) / pkhat + Math.pow((nk - nkhat), 2)
				/ nkhat;

		return delta;
	}

	private double remainder(Attribute attribute) {
		double sum = 0;
		for (String option : attribute.getOptions()) {
			double p = 0, n = 0;
			for (Example e : allExamples) {
				if (e.getClassification(attribute).equals(option)) {
					if (e.getOutput().equals(posOutput)) {
						p++;
					} else {
						n++;
					}
				}
			}
			sum += (p + n) / (totalPos + totalNeg) * b(p / (p + n));
		}
		return sum;
	}

	/*
	 * the entropy of a Boolean random variable that is true with probability q:
	 */
	private static double b(double q) {

		double a, b;
		if (q == 0) {
			a = 0;
		} else {
			a = q * log2(q);
		}

		if (q == 1) {
			b = 0;
		} else {
			b = (1 - q) * log2(1 - q);
		}

		return -(a + b);
	}

	private static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}

	public void setTreeTotalPandN(int subTreePos, int subTreeNeg) {
		totalPos = subTreePos;
		totalNeg = subTreeNeg;
	}
}
