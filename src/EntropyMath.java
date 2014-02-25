import java.util.ArrayList;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

public class EntropyMath {
	ArrayList<Example> allExamples;
	private final String posOutput = "Yes";
	int totalPos, totalNeg;

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
		return b(totalPos / ((double) totalPos + totalNeg));
	}

	public boolean relevant(Attribute attribute) {
		double delta = delta(attribute);
		int degreesOfFreedom = totalNeg + totalPos - 1;
		ChiSquaredDistribution db = new ChiSquaredDistribution(degreesOfFreedom);
		double confidence = 0.95;
		System.out.println(db.cumulativeProbability(delta));
		//System.out.println(db.cumulativeProbability(delta) <= 1 - confidence);
		return db.cumulativeProbability(delta) <= 1 - confidence;
	}

	public double delta(Attribute attribute) {

		double delta = 0;
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

			double pkhat = totalPos * (p + n) / (totalPos + totalNeg);
			double nkhat = totalNeg * (p + n) / (totalPos + totalNeg);

			delta += Math.pow((p - pkhat), 2) / pkhat
					+ Math.pow((n - nkhat), 2) / nkhat;
		}
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
}
