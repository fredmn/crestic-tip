package crestic.tip;

import static crestic.tip.prelude._diff;
import static crestic.tip.prelude._max;
import static crestic.tip.prelude._mult;
import ij.process.BinaryProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class LDM {
	
	public static FloatProcessor binaryLDM (BinaryProcessor A, BinaryProcessor B) {
		FloatProcessor rvdtA = DT.binaryDTtoForeground(A);
		FloatProcessor rvdtB = DT.binaryDTtoForeground(B);

		// abs_diffAB = |A-B|
		FloatProcessor abs_diffAB = (FloatProcessor) _diff(A.convertToFloat(), B.convertToFloat());

		// max_dtA_dtB = Max(DT_A, DT_B)
		FloatProcessor max_dtA_dtB = (FloatProcessor) _max(rvdtA, rvdtB);

		// rvdm = |A-B| * Max(DT_A, DT_B)
		FloatProcessor rvldm = (FloatProcessor) _mult(abs_diffAB, max_dtA_dtB);
		rvldm.resetMinAndMax();
		return rvldm;
	}
	
	public static FloatProcessor RVLDM (ImageProcessor A, ImageProcessor B, int cuts) {
		// todo : Faut-il normaliser A et B pour éviter une explosion des valeurs?
		FloatProcessor rvdtA = DT.RVDTtoForeground(A, cuts);
		FloatProcessor rvdtB = DT.RVDTtoForeground(B, cuts);

		// abs_diffAB = |A-B|
		FloatProcessor abs_diffAB = (FloatProcessor) _diff(A.convertToFloat(), B.convertToFloat());

		// max_dtA_dtB = Max(DT_A, DT_B)
		FloatProcessor max_dtA_dtB = (FloatProcessor) _max(rvdtA, rvdtB);

		// rvdm = |A-B| * Max(DT_A, DT_B)
		FloatProcessor rvldm = (FloatProcessor) _mult(abs_diffAB, max_dtA_dtB);
		rvldm.resetMinAndMax();
		return rvldm;
	}

	
	// todo : faire une LDM générique à la façon clojure :
	// (defn ldm [img1 img2 dt] (_* (convert-to-float (_diff img1 img2))
    //                              (_max (dt img1) (dt img2))))
	
	// Sol1 : avec interfaces et classes anomymes : type safe!
	// Sol2 : avec une classe abstraite LDMgen et des implémentations (LDMbin)

}
