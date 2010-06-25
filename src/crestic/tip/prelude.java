package crestic.tip;

import ij.process.Blitter;
import ij.process.ImageProcessor;

public class prelude {

	public static ImageProcessor _diff(ImageProcessor imgA, ImageProcessor imgB) {
		ImageProcessor result = imgA.duplicate();
		result.copyBits(imgB, 0, 0, Blitter.DIFFERENCE);
		return result;
	}

	public static ImageProcessor _max(ImageProcessor imgA, ImageProcessor imgB) {
		ImageProcessor result = imgA.duplicate();
		result.copyBits(imgB, 0, 0, Blitter.MAX);
		return result;
	}
	
	public static ImageProcessor _mult(ImageProcessor imgA, ImageProcessor imgB) {
		ImageProcessor result = imgA.duplicate();
		result.copyBits(imgB, 0, 0, Blitter.MULTIPLY);
		return result;
	}
	
}
