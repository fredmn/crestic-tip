

import crestic.tip.LDM;
import ij.*;
import ij.process.*;
import ij.gui.*;

import ij.plugin.filter.*;

// ImageJ Real Local Dissimilarity Map //
// CReSTIC - Univ. Reims-Champagne-Ardenne //
// IUT de Troyes - Made in France //
// {frederic.nicolier,jerome.landre}@univ-reims.fr //


// CLASS LDM //

public class LDM_ implements PlugInFilter {
	
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
			gd.showDialog();
			if (gd.wasCanceled()) return;

			s1Index = gd.getNextChoiceIndex();	// index of image(s) window
			s2Index = gd.getNextChoiceIndex();	// index of image(s) window

			IJ.selectWindow(wList[s1Index]);
			imgA = IJ.getImage();
			IJ.selectWindow(wList[s2Index]);
			imgB = IJ.getImage();

		}

		imp = new ImagePlus("Local Dissimilarity Map", 
				LDM.binaryLDM(new BinaryProcessor((ByteProcessor) imgA.getProcessor()),
						 new BinaryProcessor((ByteProcessor) imgB.getProcessor())));
		imp.changes=false;
		imp.show();
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

