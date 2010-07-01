package crestic.tip;

import static crestic.tip.prelude._diff;
import static crestic.tip.prelude._max;
import static crestic.tip.prelude._mult;
import ij.process.BinaryProcessor;
import ij.process.FloatProcessor;
import ij.process.FloatStatistics;
import ij.process.ImageProcessor;

public class LDM {
	
	public static FloatProcessor ldm(ImageProcessor A, ImageProcessor B, FloatProcessor dtA, FloatProcessor dtB) {
		// abs_diffAB = |A-B|
		FloatProcessor abs_diffAB = (FloatProcessor) _diff(A.convertToFloat(), B.convertToFloat());

		// max_dtA_dtB = Max(DT_A, DT_B)
		FloatProcessor max_dtA_dtB = (FloatProcessor) _max(dtA, dtB);

		// ldm = |A-B| * Max(DT_A, DT_B)
		FloatProcessor theldm = (FloatProcessor) _mult(abs_diffAB, max_dtA_dtB);
		theldm.resetMinAndMax();
		return theldm;
	}
	
	public static FloatProcessor binaryLDM(BinaryProcessor A, BinaryProcessor B) {
		return ldm(A, B, DT.binaryDTtoForeground(A), DT.binaryDTtoForeground(B));
	}
	
	public static FloatProcessor RVLDM (ImageProcessor A, ImageProcessor B, int cuts) {
//		Double m = Math.max(A.getMax(), B.getMax());
		ImageProcessor Anorm = A.convertToFloat();
		ImageProcessor Bnorm = B.convertToFloat();
		FloatStatistics Astats = new FloatStatistics(Anorm);
		FloatStatistics Bstats = new FloatStatistics(Bnorm);
		Anorm.add(- Astats.mean);
		Anorm.multiply(1.0 / Astats.stdDev);
		Bnorm.add(- Bstats.mean);
		Bnorm.multiply(1.0 / Bstats.stdDev);
		return ldm(Anorm, Bnorm, DT.RVDTtoForeground(Anorm, cuts), DT.RVDTtoForeground(Bnorm, cuts));
	}	
}