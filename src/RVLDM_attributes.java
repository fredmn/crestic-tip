import ij.*;
import ij.process.*;
import ij.io.*;
import ij.plugin.*;
import ij.measure.ResultsTable;
import java.util.ArrayList;
import java.io.*;
import java.text.*;

// ImageJ Real Valued Local Dissimilarity Map //
// Texture attributes //
// CReSTIC - Univ. Reims-Champagne-Ardenne //
// IUT de Troyes - Made in France //
// {frederic.nicolier,jerome.landre}@univ-reims.fr //

public class RVLDM_attributes implements PlugIn {

	private final static String version = "0.1";
	private final static String date = "2010-06-15"; 

	// RUN //
	public void run(String arg) {
		ImagePlus imgA; // image A
		ImagePlus imgB; // image B
		ArrayList<String> images = new ArrayList<String>();

		int cuts=32; // number of cuts in gray-levels

		// Read images list.
		OpenDialog dlg = new OpenDialog("Select images list", arg);
		String file_dir = dlg.getDirectory();
		String file = dlg.getFileName();
//		String file_dir = "/home/jlandre/actuel/recherche/libs/JL_IJ/";
//		String file = "liste_bidon.txt";
		try{
			InputStream ips=new FileInputStream(file_dir + file); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				images.add(ligne);
			}
			br.close(); 
		} catch (Exception e){
			IJ.write(e.toString());
			return;
		}
//		int Nb_images = 30;
		int Nb_images = images.size();
		IJ.write("N = " + Nb_images);
		ResultsTable res = ResultsTable.getResultsTable();
		for (int kA = 0; kA < (Nb_images - 1); kA++) {
			imgA = ij.IJ.openImage(images.get(kA));
			for (int kB = kA + 1; kB < Nb_images; kB++) {
				imgB = ij.IJ.openImage(images.get(kB));
				ComputeAttributes(imgA.getProcessor(), imgB.getProcessor(), cuts); 
				int groundTruth = ComputeGroundTruth(images.get(kA), images.get(kB));
				res.addValue("Classe", groundTruth);	
			}
		}
		//IJ.write("lignes : "+res.getCounter());
		//IJ.write("cols : "+res.getLastColumn());
		createARFFResults(res);
	}

	// ARFF Creation 
	public void createARFFResults (ResultsTable res) {

		String entetes = res.getColumnHeadings();
		String line="";
		String tempString="";
		int nb_colonnes=res.getLastColumn();
		int nb_lignes=res.getCounter();
		int i = 0, j = 0;
		double val = 0;
		
		IJ.log("%---------");
		IJ.log("%CReSTIC - IUT de Troyes");
		IJ.log("%{frederic.nicolier,jerome.landre}@univ-reims.fr");
		IJ.log("%---------");
		IJ.log("@RELATION RVLDM_attributes");
		java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(entetes, "\t");

		for (i = 0; i < nb_colonnes - 1; i++) {
			IJ.log("@ATTRIBUTE '" + tokenizer.nextToken() + "' NUMERIC");
		}
   		IJ.log("@ATTRIBUTE class {simil,diff}");
		IJ.log("@DATA");
		DecimalFormat df = new DecimalFormat("#######.######");

		for (j = 0; j < nb_lignes; j++) {
			line="";
			for (i = 0; i < nb_colonnes - 1; i++) {
				val = res.getValueAsDouble(i,j);
				tempString = df.format(val);
				line += tempString.replace(',','.');
				line += ",";
			}
			val = res.getValueAsDouble(i+1,j);
			if (val == 0.0d) line+="simil";
			else line += "diff";
			IJ.log(line);
		}
	}
	
	public int ComputeGroundTruth(String nameA, String nameB){
/*		The groundtruth is determined from the path of nameA and nameB
		Same path = similar images.*/
		int classe = 0;
		String dirA = (new File(nameA)).getParent();
		String dirB = (new File(nameB)).getParent();
		if (dirA.equals(dirB)) {
			// similar images
			classe = 1;	
		}
		return classe;
	}
	
	public void ComputeAttributes(ImageProcessor A, ImageProcessor B, int cuts) {
		// todo: use DT to foreground and not DT to background !!	
		// Resize images to same size
		ImageProcessor imgA, imgB;
		if (A.getWidth() * A.getHeight() >= B.getWidth() * B.getHeight()) {
			// A is bigger than B : resize B to A
			imgB = B.resize(A.getWidth(), A.getHeight());
			imgA = A;
		} else {
			// B is bigger then A: resize A to B
			imgA = A.resize(B.getWidth(), B.getHeight());
			imgB = B;
		}
		RVLDM_ rvldm = new RVLDM_();
		// glcm needs ByteProcessor -> scaling?
		ByteProcessor ldm = (ByteProcessor) rvldm.RVLDM(imgA, imgB, cuts).convertToByte(false); // doscaling = true
		GLCM_Texture glcm = new GLCM_Texture();
		glcm.run(ldm);
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


