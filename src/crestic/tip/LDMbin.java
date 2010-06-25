package crestic.tip;

import ij.process.BinaryProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class LDMbin extends LDMgen {

	@Override
	FloatProcessor dt(ImageProcessor A) {
		return DT.binaryDT((BinaryProcessor) A);
	}

}
