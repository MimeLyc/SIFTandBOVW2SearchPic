package image.image;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import data.HashCodeServiceImp;
import dataService.HashCodeService;

public class Phash {
	static  long m1  = 0x5555555555555555l; 
	static  long  m2  = 0x3333333333333333l; 
	static  long  m4  = 0x0f0f0f0f0f0f0f0fl;  
	static  long  m8  = 0x00ff00ff00ff00ffl;   
	static  long  m16 = 0x0000ffff0000ffffl; 
	static  long  m32 = 0x00000000ffffffffl; 
	static  long hff = 0xffffffffffffffffl; 
	static  long h01 = 0x0101010101010101l; 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashCodeService hcs=new HashCodeServiceImp();
		String basePath="D:\\data\\data_x\\data_Fdress";
		File folder=new File(basePath);
		String[] listOfImageName=folder.list();
		ArrayList<ImageHash> hashCodeList=new ArrayList<ImageHash>(listOfImageName.length);
		 for(String imageFileName:listOfImageName){
	    	 ImageHash ih=new ImageHash();
	    	String pathOfImageFile=basePath+"\\"+imageFileName;
	    	ih.setPath(pathOfImageFile);
	    	ih.setC(imageFileName.split("_")[0]);
	    	ih.setHash(pHashl(pathOfImageFile));
	    	hashCodeList.add(ih);
	    	
	     }
		 hcs.saveHashCodeToSQL(hashCodeList);
	}

	public static String pHash(String path) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Highgui.imread(path);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		Mat newImage = new Mat();
		Mat dst = new Mat();
		Size size = new Size(32, 32);
		double fx = (double) size.width / image.cols();
		double fy = (double) size.height / image.rows();
		 Imgproc.resize(image, newImage, size,fx,fy,Imgproc.INTER_AREA);
		//Imgproc.resize(image, newImage, size);
		newImage.convertTo(newImage, CvType.CV_32FC1);
		Core.dct(newImage, dst);
		double mean = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				mean = mean + dst.get(i, j)[0];
			}
		}
		mean = mean / 64;
		String l = "";
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (dst.get(i, j)[0] >= mean) {

					l = l + 1;
				} else {
					l = l + 0;
				}
			}
		}
		return l;
	}
	public static long  pHashl(String path) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Highgui.imread(path);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		Mat newImage = new Mat();
		Mat dst = new Mat();
		Size size = new Size(32, 32);
		double fx = (double) size.width / image.cols();
		double fy = (double) size.height / image.rows();
		 Imgproc.resize(image, newImage, size,fx,fy,Imgproc.INTER_AREA);
		//Imgproc.resize(image, newImage, size);
		newImage.convertTo(newImage, CvType.CV_32FC1);
		Core.dct(newImage, dst);
		double mean = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				mean = mean + dst.get(i, j)[0];
			}
		}
		mean = mean / 64;
		long l = 0;
		long x=1;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (dst.get(i, j)[0] >= mean) {
					l |= x<<(63-i*8-j);
				}
			}
		}
		return l;
	}
	public static int getWeight(long input){
		 input = (input & m1 ) + ((input >>>  1) & m1 ); //put count of each  2 bits into those  2 bits   
		 input = (input & m2 ) + ((input >>>  2) & m2 ); //put count of each  4 bits into those  4 bits   
		 input = (input & m4 ) + ((input >>>  4) & m4 ); //put count of each  8 bits into those  8 bits   
		 input = (input & m8 ) + ((input >>>  8) & m8 ); //put count of each 16 bits into those 16 bits   
		    input = (input & m16) + ((input >>> 16) & m16); //put count of each 32 bits into those 32 bits   
		    input = (input & m32) + ((input >>> 32) & m32); //put count of each 64 bits into those 64 bits   
		    return (int) input;  

	}
}
