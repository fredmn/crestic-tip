package crestic.tip;
import ij.process.*;
import ij.plugin.filter.*;

public class DT {

	public static FloatProcessor binaryDT (BinaryProcessor ip) {
		// Distance Transform via EDM Plugin
		EDM edm=new EDM(); 
		FloatProcessor dt = edm.makeFloatEDM(ip, 0, false);
		return dt;
	}

	public static FloatProcessor binaryDTtoForeground(BinaryProcessor ip) {
		BinaryProcessor img = new BinaryProcessor(ip);
		img.invert();
		return binaryDT(img);
	}

	public static FloatProcessor RVDT(ImageProcessor ip, int cuts) {
		// Real Valued Distance Transform 
		// CReSTIC - Univ. Reims-Champagne-Ardenne
		// IUT de Troyes - Made in France 
		// frederic.nicolier@univ-reims.fr, jerome.landre@univ-reims.fr 

		// from I. S. Molchanov and P. Ter‡n, "Distance transforms for
		// real-valued functions," J. Math. Anal. Appl., iss. 278,
		// pp. 472-484, 2003.

		FloatProcessor ipFloat, fpResult;
		BinaryProcessor ipThresholded;

		int width = ip.getWidth();
		int height = ip.getHeight();

		ipFloat = (FloatProcessor) ip.convertToFloat();
		float[] ipPixels = (float []) ipFloat.getPixels();

		float min = (float) ipFloat.getMin();
		float max = (float) ipFloat.getMax();

		float step = (max-min)/(cuts+1); // step that defines intervals of cuts

		fpResult = new FloatProcessor(width, height);
		fpResult.and(0);
		for (float threshold = step + min; threshold < max; threshold += step) {
			// for each gray-level cut
			ipThresholded = new BinaryProcessor(new ByteProcessor(width, height));
			byte[] ThresholdedPixels=(byte[]) ipThresholded.getPixels();
			for (int index=0; index<width*height; index++) {
				if ((ipPixels[index])>threshold) {
					ThresholdedPixels[index]=(byte) 0xFF;
				}
				else ThresholdedPixels[index]=(byte) 0;
			}

			FloatProcessor dt = DT.binaryDT(ipThresholded);
			fpResult.copyBits(dt,0,0,Blitter.ADD); // updates fpResult
		}
		fpResult.multiply(1.0/cuts);
		fpResult.resetMinAndMax(); 
		return fpResult;
	}

	public static FloatProcessor RVDTtoForeground(ImageProcessor ip, int cuts) {
		// Real Valued Distance Transform 
		// CReSTIC - Univ. Reims-Champagne-Ardenne
		// IUT de Troyes - Made in France 
		// frederic.nicolier@univ-reims.fr, jerome.landre@univ-reims.fr 

		// from I. S. Molchanov and P. Ter‡n, "Distance transforms for
		// real-valued functions," J. Math. Anal. Appl., iss. 278,
		// pp. 472-484, 2003.

		int width = ip.getWidth();
		int height = ip.getHeight();

		FloatProcessor ipFloat = (FloatProcessor) ip.convertToFloat();
		float[] ipPixels = (float []) ipFloat.getPixels();

		float min = (float) ipFloat.getMin();
		float max = (float) ipFloat.getMax();

		float step = (max - min) / (cuts + 1); // step that defines intervals of cuts

		FloatProcessor fpResult = new FloatProcessor(width, height);
		fpResult.and(0);
		for (float threshold = step + min; threshold < max; threshold += step) {
			// for each gray-level cut
			BinaryProcessor ipThresholded = new BinaryProcessor(new ByteProcessor(width, height));
			byte[] ThresholdedPixels = (byte[]) ipThresholded.getPixels();
			for (int index = 0; index < width * height; index++) {
				if ((ipPixels[index]) > threshold) {
					ThresholdedPixels[index] = (byte) 0xFF;
				}
				else ThresholdedPixels[index] = (byte) 0;
			}

			FloatProcessor dt = DT.binaryDTtoForeground(ipThresholded);
			fpResult.copyBits(dt, 0, 0, Blitter.ADD); // updates fpResult
		}
		fpResult.multiply(1.0 / cuts);
		fpResult.resetMinAndMax(); 
		return fpResult;
	}

}
