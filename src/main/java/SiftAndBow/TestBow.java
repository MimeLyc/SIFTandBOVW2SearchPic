package SiftAndBow;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

/**
 * Created by QQQ on 2017/4/6.
 */
public class TestBow {


	private static int K = 1000;
	public Mat clustering(Mat sourse){
//		Core core = new Core();
//		Mat result = new Mat(K,128,5);
		Mat result = new Mat();
//		System.out.println(result.dims());
		double test = Core.kmeans(sourse,K,result, new TermCriteria(TermCriteria.COUNT,1000,5),1,Core.KMEANS_PP_CENTERS);
//		System.out.println(test);
//		System.out.println(result.rows());
		return result;

	}

//	public static void main(String[] args){
//		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
//		ExtractSift test = new ExtractSift();
////		Mat trainDesc = test.extractAllDesc("D:\\GraduationProject\\data_x\\dress_kmeansTrainer\\128_0.jpg");
//		System.out.println(trainDesc.rows()+"/"+trainDesc.cols()+"/"+trainDesc.dims());
//
//		double[][] testK = new double[trainDesc.rows()][trainDesc.cols()];
//		for(int i = 0;i<trainDesc.rows();i++){
//			for (int j=0;j<trainDesc.cols();j++){
//				testK[i][j] = trainDesc.get(i,j)[0];
////				System.out.print(trainDesc.get(i,j)[0]);
//			}
////			System.out.println();
//
//		}
//
//		//			Features2d.drawKeypoints(test_mat,mkp,desc);
////			Highgui.imwrite("output//outputImage.jpg",desc);
//
////		MyKmeans testKm = new MyKmeans();
////		testKm.setK(100);
////		testKm.cluster(testK);
////		double[][] dictionary = BowTrainer();
//
//
//
////		TestBow testB = new TestBow();
////
////		Mat result = new Mat(K,128,5);
////		result = testB.clustering(trainDesc);
////
//
//
//
//
//
////		System.out.println(testB.clustering(trainDesc).rows());
////		System.out.println(result.rows()+"/"+result.cols());
////		for(int i=0;i<result.rows();i++){
////			System.out.print(result.get(i,0).length+" ");
////		}
////		System.out.println(result.get(0,0).length);
//
//	}

//	Core.kmeans();
//	BOWTrainer trainner = new BOWTrainer();
}
