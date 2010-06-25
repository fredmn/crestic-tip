

import crestic.tip.DT;
import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

// ImageJ Real Valued Disce Transform GLDT //
// CReSTIC - Univ. Reims-Champagne-Ardenne //
// IUT de Troyes - Made in France //
// frederic.nicolier@univ-reims.fr, jerome.landre@univ-reims.fr //

// from I. S. Molchanov and P. Ter‡n, "Distance transforms for
// real-valued functions," J. Math. Anal. Appl., iss. 278,
// pp. 472-484, 2003.

// CLASSE RVDT //
// Real Valued Distance Transform //
// uses EDM Plugin //
public class RVDT_ implements PlugInFilter {

	ImagePlus imp;
	private final static String version = "0.5";
	private final static String date = "2010-03-29"; 


	// SETUP //
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G+DOES_16+DOES_32;
	}


	// RUN //
	public void run(ImageProcessor ip) {

		int cuts=32; // number of cuts in gray-levels
		int i=0;
		int[] wList = WindowManager.getIDList();

		if (wList==null) {
			IJ.showMessage("Image", "You need at least one open image to run this plugin...");
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
			gd.addChoice("Image(s) window: ", titles, titles[0]);
			gd.addNumericField("number of cuts: ", cuts, 0);
			gd.showDialog();
			if (gd.wasCanceled()) return;

			int s1Index=gd.getNextChoiceIndex();	// index of image(s) window
			cuts = (int)gd.getNextNumber();

			IJ.selectWindow(wList[s1Index]);
		}
		imp = new ImagePlus("RVDT", DT.RVDT(ip, cuts));
		IJ.showStatus("Result image");
		imp.changes=false;
		imp.show();
	}

	// printStatus //
	private void printStatus() {

		IJ.write("");
		IJ.write("-------------------------------------");
		IJ.write("--- Real Valued Distance Transform ---");
		IJ.write("-------------------------------------");
		IJ.write("");
		IJ.write("CReSTIC --- IUT de Troyes");
		IJ.write("Universite de Reims-Champagne-Ardenne");
		IJ.write("version "+version+" - "+date);
		IJ.write("");
		IJ.write("from I. S. Molchanov and P. Ter‡n, Distance transforms for");
		IJ.write(" real-valued functions, J. Math. Anal. Appl., iss. 278,");
		IJ.write(" pp. 472-484, 2003.");
		IJ.write("-------------------------------------------------------------");
		IJ.write("frederic.nicolier@univ-reims.fr, jerome.landre@univ-reims.fr");	

	}
}
