import java.util.ArrayList;

public class EntropyMath {
	ArrayList<Example> allExamples;
	private final String posOutput = "Yes";
	double totalPos, totalNeg;

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

	public double calculateGain(Attribute attribute) {
		return totalEntropy() - remainder(attribute);
	}

	/* The total entropy */
	private double totalEntropy() {
		return b(totalPos / (totalPos + totalNeg));
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
}
