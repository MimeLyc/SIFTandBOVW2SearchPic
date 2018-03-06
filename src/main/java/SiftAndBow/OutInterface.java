package SiftAndBow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QQQ on 2017/4/18.
 */
public class OutInterface {

	static ClusterPic cluster = new ClusterPic();
	HashMap<String,double[]> result = new HashMap<>();

	public void trainATest(String trainPath,String testPath,int K) throws IOException {

		cluster.setDictionary(trainPath,K);
		double[][] testDic = cluster.getDictionary();
		for (int i = 0;i<testDic.length;i++){
			for (int j=0;j<testDic[0].length;j++){
				System.out.print(testDic[i][j]);
			}
			System.out.println();
		}


		result = cluster.getPicWord(testPath);

		for(Map.Entry<String,double[]> pic:result.entrySet()
				){
			System.out.println(pic.getKey()+":");
			double[] word = pic.getValue().clone();
			for (double d:word){
				System.out.print(d+";");
			}
			System.out.println();
		}
	}


	public ArrayList<String> getTopF(String picPath) throws IOException {



		HashMap<String,double[]> toFind = cluster.getPicWord(picPath);
		double[] picWord = toFind.get(picPath);
		double minest = 200000000;
		ArrayList<Double> minDis = new ArrayList<>();
		for (int i = 0;i<6;i++){
			minDis.add(i, (double) 200000000);
		}
		ArrayList<String> topFive = new ArrayList<String>();
		for(Map.Entry<String,double[]> pic:result.entrySet()
				){
			double temp = cluster.calDis(pic.getValue().clone(),picWord);
			if (temp< minest){
				minest = temp;
				topFive.add(0,pic.getKey());
				minDis.add(0,temp);
			}else{
				for(int i =0;i<5;i++){
					if(temp>minDis.get(i)&&temp<minDis.get(i+1)){
						minDis.add(i+1,temp);
						topFive.add(i+1,pic.getKey());
					}

				}
			}
		}

//		for (int i=0;i<5;i++){
//			System.out.println("Match "+i+":"+topFive.get(i));
//		}

		return topFive;

	}

	/**
	 * 将图片聚类到指定目录下,示例模仿ClusterPic main方法，
	 * @param trainDir	训练词典的目录
	 * @param fromPath	需要聚类的图片的目录
	 * @param toPath	聚类的目标目录
	 * @param dirK	词典个数
	 * @param clusterK 希望图片聚成类的个数
	 * @throws IOException
	 */
	public void clusterToDir(String trainDir,String fromPath,String toPath,int dirK,int clusterK) throws IOException {
		cluster = new ClusterPic();
		cluster.setDictionary(trainDir,dirK);
		cluster.setPicWordToCluster(fromPath);
		cluster.cluster(toPath,clusterK);
	};

	public static void main(String[] args) throws IOException {
		OutInterface test = new OutInterface();
		test.trainATest("D:\\GraduationProject\\data_x\\TestBow\\train","D:\\GraduationProject\\data_x\\TestBow\\test_1",30);
		ArrayList<String> f = test.getTopF("D:\\GraduationProject\\data_x\\TestBow\\126_39.jpg");
		for (int i=0;i<f.size();i++){
			System.out.println(f.get(i));
		}


		//		test.clusterToDir("D:\\GraduationProject\\data_x\\TestBow\\train","D:\\GraduationProject\\data_x\\TestBow\\test_1","D:\\GraduationProject\\data_x\\TestBow\\Output",30,5);


//		File toClear = new File("D:\\GraduationProject\\data_x\\TestBow\\Output");
//		File[] fileList = toClear.listFiles();
//		for (File f:fileList){
//			f.delete();
//		}








		//		test.trainATest("D:\\GraduationProject\\data_x\\TestBow\\train","D:\\GraduationProject\\data_x\\TestBow\\test_1",30);
//		ArrayList<String> tpf = test.getTopF("D:\\GraduationProject\\data_x\\TestBow\\159_7.jpg");
//		cluster.setAllPicMat();
//		test.clusterToDir("D:\\GraduationProject\\data_x\\TestBow\\train");



	}

}
