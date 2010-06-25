package crestic.tip;

import static crestic.tip.prelude._diff;
import static crestic.tip.prelude._max;
import static crestic.tip.prelude._mult;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public abstract class LDMgen {
	abstract FloatProcessor dt (ImageProcessor A);
	
	public FloatProcessor f(ImageProcessor A, ImageProcessor B) {
		FloatProcessor dtA = dt(A);
		FloatProcessor dtB = dt(B);
		
		// abs_diffAB = |A-B|
		FloatProcessor abs_diffAB = (FloatProcessor) _diff(A.convertToFloat(), B.convertToFloat());

		// max_dtA_dtB = Max(DT_A, DT_B)
		FloatProcessor max_dtA_dtB = (FloatProcessor) _max(dtA, dtB);

		// rvdm = |A-B| * Max(DT_A, DT_B)
		FloatProcessor ldm = (FloatProcessor) _mult(abs_diffAB, max_dtA_dtB);
		ldm.resetMinAndMax();
		return ldm;
	}
	
}
