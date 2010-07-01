import crestic.tip.DT;
import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

// CReSTIC - Univ. Reims-Champagne-Ardenne //
// IUT de Troyes - Made in France //
// frederic.nicolier@univ-reims.fr, jerome.landre@univ-reims.fr //


public class DT_ implements PlugInFilter {

	ImagePlus imp;
	private final static String version = "0.5";
	private final static String date = "2010-03-29"; 


	// SETUP //
	public int setup(String arg, ImagePlus imp) {
		this.imp = imp;
		return DOES_8G;
	}

	public void run(ImageProcessor ip) {

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
			gd.showDialog();
			if (gd.wasCanceled()) return;

			int s1Index=gd.getNextChoiceIndex();	// index of image(s) window
			IJ.selectWindow(wList[s1Index]);
		}
		imp = new ImagePlus("DT", DT.binaryDTtoForeground(new BinaryProcessor((ByteProcessor)ip)));
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
		IJ.write("from I. S. Molchanov and P. Teran, Distance transforms for");
		IJ.write(" real-valued functions, J. Math. Anal. Appl., iss. 278,");
		IJ.write(" pp. 472-484, 2003.");
		IJ.write("-------------------------------------------------------------");
		IJ.write("frederic.nicolier@univ-reims.fr, jerome.landre@univ-reims.fr");	

	}
}
