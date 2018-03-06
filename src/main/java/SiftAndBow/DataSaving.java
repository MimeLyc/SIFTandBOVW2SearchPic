package SiftAndBow;

import data.DirServiceImp;
import data.WordServiceImp;
import dataService.Dirservice;
import dataService.WordService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by QQQ on 2017/4/24.
 */
public class DataSaving {

	private ClusterPic cluster = new ClusterPic();
	public void saveDir(String dirPath,int K) throws IOException {
		cluster.setDictionary(dirPath,K);
		double[][] dictionary = cluster.getDictionary();

//		for (int i = 0;i<dictionary.length;i++){
//			for (int j=0;j<dictionary[0].length;j++){
//				System.out.print(dictionary[i][j]);
//			}
//			System.out.println();
//		}

		Dirservice ds = new DirServiceImp();
		ds.saveDirToSQL(dictionary);
//		for (int i = 0;i<testDic.length;i++){
//			for (int j=0;j<testDic[0].length;j++){
//				System.out.print(testDic[i][j]);
//			}
//			System.out.println();
//		}
	}

	private void setDir(){

		Dirservice ds = new DirServiceImp();
		double[][] dir = ds.getDir();
		cluster.setDictionary(dir);

	}

	public void saveWords(String picPath) throws IOException {
		HashMap<String,double[]> words = new HashMap<>();
		WordService ws = new WordServiceImp();
//		File dir = new File(picPath);
		List<File> fileList = cluster.getImgFiles(picPath);

		int count = 0;
		int tempC = 0;
		System.out.println(fileList.size());
		for (int i=0;i<fileList.size();i++,count++){

			String key = fileList.get(i).getPath();
			double[] word = cluster.getPicWord(key).get(key);
			words.put(key,word);
			if (count>=200||i == fileList.size()-1){
				ws.saveWordToSQL(words);
				words.clear();
				count =0;
			}

			tempC++;
			System.out.println(tempC);
		}



//		words = cluster.getPicWord(picPath);
//		WordService ws = new WordServiceImp();

	}


	public static void main(String[] args) throws IOException {
		DataSaving dt = new DataSaving();
		dt.saveDir("D:\\GraduationProject\\data_x\\TestBow\\train",50);
//		dt.setDir();

		long startP = System.nanoTime();
		dt.saveWords("D:\\GraduationProject\\data_x\\data_x");
		long endP = System.nanoTime();
		double msP = (endP - startP) / 1000000d;
		System.out.printf("Saving all picture words cost %,.3f ms%n",msP);


	}



}
