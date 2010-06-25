

import crestic.tip.DT;
import crestic.tip.LDM;
import ij.*;
import ij.process.*;
import ij.gui.*;

import ij.plugin.filter.*;

// ImageJ Real Valued Local Dissimilarity Map //
// CReSTIC - Univ. Reims-Champagne-Ardenne //
// IUT de Troyes - Made in France //
// {frederic.nicolier,jerome.landre}@univ-reims.fr //


// CLASS RVLDM //
// Real Valued Local Dissimilarity Map //
// uses RVDT Plugin //
public class RVLDM_ implements PlugInFilter {

	ImagePlus imp;
	private final static String version = "0.1";
	private final static String date = "2010-04-27"; 

	// SETUP //
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}


	// RUN //
	public void run(ImageProcessor ip) {

		ImagePlus imgA; // image A
		ImagePlus imgB; // image B

		int s1Index=0;	// image 1 index in ImageJ list
		int s2Index=0;	// image 2 index in ImageJ list
		int cuts=32; // number of cuts in gray-levels
		int i=0;
		int[] wList = WindowManager.getIDList();

		if (wList.length<2) {
			IJ.showMessage("Image", "You need at least two open images to run this plugin...");
			return;
		} 
		else {
			String[] titles = new String[wList.length];
			for (i=0; i<wList.length; i++) {
				ImagePlus imp = WindowManager.getImage(wList[i]);
				if (imp!=null) {
					titles[i] = imp.getTitle();
					titles[i] = titles[i]+"-"+imp.getImageStackSize();
				} 
				else {
					titles[i] = "window";
				}
			}

			GenericDialog gd = new GenericDialog(":");
			gd.addChoice("Image A: ", titles, titles[0]);
			gd.addChoice("Image B: ", titles, titles[1]);
			gd.addNumericField("number of cuts: ", (double) cuts, 0);
			gd.showDialog();
			if (gd.wasCanceled()) return;

			s1Index=gd.getNextChoiceIndex();	// index of image(s) window
			s2Index=gd.getNextChoiceIndex();	// index of image(s) window
			cuts = (int)gd.getNextNumber();

			IJ.selectWindow(wList[s1Index]);
			imgA = IJ.getImage();
			IJ.selectWindow(wList[s2Index]);
			imgB = IJ.getImage();

		}

		printStatus();

		imp = new ImagePlus("Real Valued Local Dissimilarity Map", 
				LDM.RVLDM(imgA.getProcessor(), imgB.getProcessor(), cuts));
		IJ.showStatus("Result image");
		imp.changes=false;
		imp.show();
	}


	// BLDM //
	public FloatProcessor RVLDM(ImageProcessor A, ImageProcessor B, int cuts) {
		FloatProcessor rvdt_A= DT.RVDT(A, cuts);
		FloatProcessor rvdt_B= DT.RVDT(B, cuts);

		// abs_diffAB = |A-B|
		FloatProcessor abs_diffAB = (FloatProcessor) A.convertToFloat();
		abs_diffAB.copyBits(B.convertToFloat(), 0, 0, Blitter.DIFFERENCE);

		// max_dtA_dtB = Max(DT_A, DT_B)
		FloatProcessor max_dtA_dtB = (FloatProcessor) rvdt_A.duplicate();
		max_dtA_dtB.copyBits(rvdt_B, 0, 0, Blitter.MAX);

		// rvdm = |A-B| * Max(DT_A, DT_B)
		FloatProcessor rvldm = (FloatProcessor) abs_diffAB.duplicate();
		rvldm.copyBits(max_dtA_dtB, 0, 0, Blitter.MULTIPLY);
		rvldm.resetMinAndMax();
		return(rvldm);
	}

	// printStatus //
	private void printStatus() {

		IJ.write("");
		IJ.write("");
		IJ.write("");
		IJ.write("-----------------------------------------");
		IJ.write("--- Real Valued Local Dissimilarity Map ---");
		IJ.write("-----------------------------------------");
		IJ.write("");
		IJ.write("CReSTIC --- IUT de Troyes");
		IJ.write("Universite de Reims-Champagne-Ardenne");
		IJ.write("version "+version+" - "+date);
		IJ.write("{frederic.nicolier,jerome.landre}@univ-reims.fr");	
		IJ.write("");
		IJ.write("");
		IJ.write("");
	}
}

