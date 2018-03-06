package SiftAndBow;  /**
 * Created by QQQ on 2017/4/11.
 */

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将图片进行聚类
 */
public class ClusterPic {



//	存储图片sift信息
	private HashMap<String,double[][]> picMat = new HashMap<>();
//	根据BOW，存取图片单词表
	private HashMap<String,double[]> picWord = new HashMap<>();

	private double[][] dictionary ;
	static List<File> imgFile = new ArrayList<File>();

	public ClusterPic(){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	}

	public List<File> getImgFiles(String path) throws IOException {
//		ArrayList<File> files = new ArrayList<File>();
		List<File> files = new ArrayList<File>();
		List<File> dirs = new ArrayList<File>();
		File file = new File(path);
		if (file.isDirectory()) {
			dirs.add(file);
		}else{
			files.add(file);
		}

		while (dirs.size() > 0) {
			for (File temp : dirs.remove(0).listFiles()) {
				if (temp.isDirectory()) {
					dirs.add(temp);
				} else {
					files.add(temp);
				}
			}
		}

		return files;

	}

	private double[][] convertMat(Mat pic){
		double[][] picD = new double[pic.rows()][pic.cols()];
		for (int i=0;i<pic.rows();i++){
			for (int j=0;j<pic.cols();j++){
				picD[i][j] = pic.get(i,j)[0];
			}
		}
		return picD;


	}

	/**
	 * 设置图片集合
	 * @param dic
	 * @throws IOException
	 */
	public void setAllPicMat(String dic) throws IOException {
		picMat.clear();
		imgFile = this.getImgFiles(dic);
		ExtractSift extractor = new ExtractSift();
		for (File file:imgFile
			 ) {

			//TODO
			long startP = System.nanoTime();
			double[][] picD = this.convertMat(extractor.extractDesc(file));
			picMat.put(file.getPath(),picD);

			long endP = System.nanoTime();
			double msP = (endP - startP) / 1000000d;
//			System.out.printf("Sift cost %,.3f ms%n",msP);

			System.out.println("Test:getSift of+"+file.getName()+" over.Cost "+msP+"ms");

		}



	}

	/**
	 * 根据训练集提取词典
	 * @param trainPath 训练集目录
	 * @param K 提取出K个单词
	 * @throws IOException
	 */
	public void setDictionary(String trainPath,int K) throws IOException {
		List<File> trainSet = new ArrayList<File>();
		trainSet = this.getImgFiles((trainPath));
		ExtractSift extractor = new ExtractSift();
		ArrayList<double[][]> siftSet = new ArrayList<>();
		int countRow = 0;
		System.out.println(trainPath+"?");
		for (File file:trainSet
				) {

			long startP = System.nanoTime();
			double[][] picD = this.convertMat(extractor.extractDesc(file));

			long endP = System.nanoTime();
			double msP = (endP - startP) / 1000000d;
//			System.out.printf("Sift cost %,.3f ms%n",msP);

			System.out.println("Train:getSift of+"+file.getName()+" over.Cost "+msP+"ms");
//			picMat.put(file.getPath(),picD);
			siftSet.add(picD);
			countRow+= picD.length;

		}
		System.out.println(siftSet.size()+"TrainSiftSize");
		double[][] trainPSet = new double[countRow][siftSet.get(0)[0].length];
		int countIndex = 0;
		for(int i=0;i<siftSet.size();i++){
			for(int j = 0;j<siftSet.get(i).length;j++){
				trainPSet[countIndex] = siftSet.get(i)[j];
				countIndex++;
			}
		}

		BowTrainer trainer = new BowTrainer(K,trainPSet);
		dictionary = trainer.getDictionary();



	}


	public void setDictionary(double[][] dic){
		this.dictionary = dic;
	}

	private HashMap<String,double[][]> getPicMat(String dic) throws IOException {
		HashMap<String,double[][]> result = new HashMap<>();
		List<File> imgFiles = this.getImgFiles(dic);
		ExtractSift extractor = new ExtractSift();
		for (File file:imgFiles
				) {

			//TODO
			long startP = System.nanoTime();
			double[][] picD = this.convertMat(extractor.extractDesc(file));
			result.put(file.getPath(),picD);

			long endP = System.nanoTime();
			double msP = (endP - startP) / 1000000d;
//			System.out.printf("Sift cost %,.3f ms%n",msP);

			System.out.println("getSift of+"+file.getName()+" over.Cost "+msP+"ms");

		}

		return result;
	}


	/**
	 * 获取每张图片的单词
	 * @param dic 图片文件夹地址
	 * @return	图片对应的单词
	 * @throws IOException
	 */
	public HashMap<String,double[]> getPicWord(String dic) throws IOException {
		HashMap<String,double[][]> picMat = this.getPicMat(dic);
		HashMap<String,double[]> result = new HashMap<>();

		BowExtractor extractor = new BowExtractor();
		extractor.setDictionary(this.dictionary);

		for (Map.Entry<String,double[][]> pic:picMat.entrySet()
			 ) {

			long startP = System.nanoTime();
			double[] word = extractor.getAPicBow(pic.getValue());
			result.put(pic.getKey(),word);
			long endP = System.nanoTime();
			double msP = (endP - startP) / 1000000d;

			System.out.println(pic.getKey()+":extract words done,cost:"+msP);
		}

		return result;
	}

	public double[][] getDictionary(){
		return this.dictionary;
	}






	public void setPicWordToCluster(String dir) throws IOException {

		this.picWord = this.getPicWord(dir);

	}

//	TODO
	/**
	 * 将图片聚类到指定文件夹下,需要先执行setPicWordToCluster
	 * @param toPath 指定的目录
	 */
	public void cluster(String toPath,int K) throws IOException {
//		this.picWord = this.getPicWord();
		int size = this.picWord.size();
		int length = this.dictionary.length;
		double[][] data = new double[size][length];
		String[] dir = new String[size];
		int index = 0;
		for (Map.Entry<String,double[]> map:this.picWord.entrySet()
			 ) {
			dir[index] = map.getKey();
			data[index] = map.getValue();
			index++;

		}
		MyKmeans cluster = new MyKmeans(K,data);
		cluster.cluster(data);
		HashMap<Integer,List<Integer>> indexMap = cluster.getIndexMap();
		this.copyToDir(dir,toPath,indexMap);



	}

	private void deleteFile(File file){
		if(file.isFile() || file.list().length ==0)
		{
			file.delete();
		}else{
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
				files[i].delete();
			}


			if(file.exists())         //如果文件本身就是目录 ，就要删除目录
				file.delete();
		}
	}


	/**
	 * 将sourceP指定的文件拷贝到toDir目录下，按index的类别存放
	 * @param sourceP
	 * @param toDir
	 * @param index
	 */
	private void copyToDir(String[] sourceP,String toDir,HashMap<Integer,List<Integer>> index) throws IOException {

		File toClear = new File(toDir);
		File[] fileList = toClear.listFiles();
		for (File f:fileList){
			this.deleteFile(f);
		}
		for (Map.Entry<Integer,List<Integer>> pic:index.entrySet()){



			String finalDir = toDir+"\\"+pic.getKey();


			File dir = new File(finalDir);
			dir.mkdir();
			finalDir = finalDir+"\\";
			for (int i=0;i<pic.getValue().size();i++){
				String fromPath = sourceP[pic.getValue().get(i)];
				String toName = sourceP[pic.getValue().get(i)];
				toName = toName.split("\\\\")[toName.split("\\\\").length-1];
				String toPath = finalDir+toName;

				System.out.println("from:"+fromPath);
				Mat picture = Highgui.imread(fromPath);
				System.out.println("to:"+toPath);
				Highgui.imwrite(toPath,picture);



			}

		}


	}


	/**
	 * 将sourceP指定的文件拷贝到toDir目录下
	 * @param sourceP
	 * @param toDir
	 */
	public void copyToDir(String sourceP,String toDir) throws IOException {

//		File toClear = new File(toDir);
//		File[] fileList = toClear.listFiles();
//		for (File f:fileList){
//			this.deleteFile(f);
//		}
//		for (Map.Entry<Integer,List<Integer>> pic:index.entrySet()){

			int sourceLength = sourceP.split("\\\\").length;

			String finalFile = toDir+"\\"+sourceP.split("\\\\")[sourceLength-1];


//			File dir = new File(finalDir);
//			dir.mkdir();
//			finalDir = finalDir+"\\";
				String fromPath = sourceP;
//				String toName = sourceP[pic.getValue().get(i)];
//				toName = toName.split("\\\\")[toName.split("\\\\").length-1];
				String toPath = finalFile;

				System.out.println("from:"+fromPath);
				Mat picture = Highgui.imread(fromPath);
				System.out.println("to:"+toPath);
				Highgui.imwrite(toPath,picture);


//		}


	}
	/**
	 * 计算欧式距离
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	double calDis(double[] arr1, double[] arr2){
		double result = 0;
		for(int i=0;i<arr1.length;i++){
			result += Math.pow((arr1[i]-arr2[i]),2);
		}

		return Math.sqrt(result);

	}


	private void setWordTTXT(HashMap<String,double[]> map){

	}


	public static void main(String[] args) throws IOException {
		ClusterPic test = new ClusterPic();
		test.setDictionary("D:\\GraduationProject\\data_x\\TestBow\\train",30);
		test.setPicWordToCluster("D:\\GraduationProject\\data_x\\TestBow\\test_1");
		test.cluster("D:\\GraduationProject\\data_x\\TestBow\\Output",10);
	}



//	public static void main(String[] args) throws IOException {
//		ClusterPic test = new ClusterPic();
//		test.setDictionary("D:\\GraduationProject\\data_x\\TestBow\\train",30);
//		double[][] testDic = test.getDictionary();
//		for (int i = 0;i<testDic.length;i++){
//			for (int j=0;j<testDic[0].length;j++){
//				System.out.print(testDic[i][j]);
//			}
//			System.out.println();
//		}
//
//
//		HashMap<String,double[]> result = test.getPicWord("D:\\GraduationProject\\data_x\\TestBow\\test_1");
//
//		for(Map.Entry<String,double[]> pic:result.entrySet()
//				){
//			System.out.println(pic.getKey()+":");
//			double[] word = pic.getValue().clone();
//			for (double d:word){
//				System.out.print(d+";");
//			}
//			System.out.println();
//		}
//
//		HashMap<String,double[]> toFind = test.getPicWord("D:\\GraduationProject\\data_x\\TestBow\\159_7.jpg");
//		double[] picWord = toFind.get("D:\\GraduationProject\\data_x\\TestBow\\159_7.jpg");
//		double minest = 200000000;
//		ArrayList<Double> minDis = new ArrayList<>();
//		for (int i = 0;i<6;i++){
//			minDis.add(i, (double) 200000000);
//		}
//		ArrayList<String> topFive = new ArrayList<String>();
//		for(Map.Entry<String,double[]> pic:result.entrySet()
//				){
//			double temp = test.calDis(pic.getValue().clone(),picWord);
//			if (temp< minest){
//				minest = temp;
//				topFive.add(0,pic.getKey());
//				minDis.add(0,temp);
//			}else{
//				for(int i =0;i<5;i++){
//					if(temp>minDis.get(i)&&temp<minDis.get(i+1)){
//						minDis.add(i+1,temp);
//						topFive.add(i+1,pic.getKey());
//					}
//
//				}
//			}
//		}
//
//		for (int i=0;i<5;i++){
//			System.out.println("Match "+i+":"+topFive.get(i));
//		}
////		System.out.println(matchPic+" match best.");
//
//	}


}
