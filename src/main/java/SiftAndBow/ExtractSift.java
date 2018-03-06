package SiftAndBow;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.features2d.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractSift
{



	public Mat extractDesc(File  img){

		File imgFile = img;
		Mat test_mat = Highgui.imread(imgFile.getPath());
		Mat desc = new Mat();

		FeatureDetector fd = FeatureDetector.create(FeatureDetector.SIFT);
		MatOfKeyPoint mkp =new MatOfKeyPoint();
		fd.detect(test_mat, mkp);
		DescriptorExtractor de = DescriptorExtractor.create(DescriptorExtractor.SIFT);
		de.compute(test_mat,mkp,desc );//提取sift特征
		return desc;

	}


	public static void main( String[] args )
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		ExtractSift test = new ExtractSift();
//		ArrayList<Double> trainDesc = new ArrayList<Double>();
//		Mat trainDesc = test.extractAllDesc("D:\\GraduationProject\\data_x\\dress_kmeansTrainer");
//		System.out.println(trainDesc.rows());
//		ExtractSift test = new ExtractSift();
//		try {
//			test.getImgFiles("D:\\GraduationProject\\data_x\\data_x\\data_food");
////			test.getImgFiles("imgs");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//
//		for (File file:imgFile
//			 ) {

//开始时间
			long startP = System.nanoTime();

			File file = new File("D:\\GraduationProject\\data_x\\data_x\\data_baby\\184_2.jpg");
			Mat test_mat = Highgui.imread(file.getPath());
			Mat desc = new Mat();
			FeatureDetector fd = FeatureDetector.create(FeatureDetector.SIFT);
			MatOfKeyPoint mkp =new MatOfKeyPoint();
			fd.detect(test_mat, mkp);
			DescriptorExtractor de = DescriptorExtractor.create(DescriptorExtractor.SIFT);
			de.compute(test_mat,mkp,desc );//提取sift特征

//			System.out.println(desc.cols());
//			System.out.println(desc.rows());


//
//			String filePath = "D:\\GraduationProject\\sift+lbp\\TestOpencvSift\\output\\feature\\"+file.getName().split("\\.")[0]+".txt";
//			File Ofile=new File(filePath);
//
//			try(BufferedWriter out = new BufferedWriter(new FileWriter(Ofile));  ){
//			//	如果追加方式用true
//
//						//	每个关键点
//				for(int j=0;j<desc.rows();j++){
//					for(int i=0;i<desc.cols();i++){
//					//				String filePath = fatherPath+imgFile.get(i).getName().split(".")[0]+".txt";
//					//				File file=new File(fatherPath);
//
////					for(int j=0;j<desc.rows();j++){
//						out.write((Double.toString(desc.get(j,i)[0])));//注意需要转换对应的字符集
//						//					out.close();
//					}
//					//				FileOutputStream out=new FileOutputStream(file,false); //如果追加方式用true
////						KDFeaturePoint  feature= al.get(j);
//					out.newLine();
//				}
//					out.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			for(int i=0;i<desc.cols();i++){
//				for(int j=0;j<desc.rows();j++){
//					System.out.print(desc.get(j,i)[0]);
//
//				}
//				System.out.println();
//			}
			Features2d.drawKeypoints(test_mat,mkp,desc);
			Highgui.imwrite("D:\\GraduationProject\\Project\\image\\output\\test.jpg",desc);
//结束时间

			long endP = System.nanoTime();
			double msP = (endP - startP) / 1000000d;
			System.out.printf("Sift cost %,.3f ms%n",msP);
//		}

	}
}