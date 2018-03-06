package SiftAndBow; /**
 * Created by QQQ on 2017/4/10.
 */

import org.opencv.core.Mat;

/**
 * 构造图片词典
 */
public class BowExtractor {


	private double[][] dictionary;

	/**
	 * 计算欧式距离
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private double calDis(double[] arr1,double[] arr2){
		if(arr1.length!=arr2.length)
			throw new java.lang.IllegalArgumentException("Arr should be in the same dim");

		double result = 0;
		for(int i=0;i<arr1.length;i++){
			result += Math.pow((arr1[i]-arr2[i]),2);
		}

		return Math.sqrt(result);
	}

	/**
	 * 首先设置词典
	 * @param dictionary
	 */
	public void setDictionary(double[][] dictionary){
		this.dictionary = dictionary;
	}

	/**
	 * 获取一张图片的词典
 	 * @param pic 图片信息，表示成一个二维Double向量
	 * @return 图片的词典
	 */
	public double[] getAPicBow(double[][] pic){
		if(dictionary==null)
			throw new java.lang.IllegalArgumentException("Should set dictionary first");

		double[] extractor = new double[dictionary.length];

		for(int i=0;i<extractor.length;i++){
			extractor[i] = 0;
		}


		for(double[] arr:pic){
			double minDis = calDis(arr,dictionary[0]);
			int index = 0;
			for (int i=0;i<dictionary.length;i++) {
				double temp = calDis(arr,dictionary[i]);
				if (temp<minDis){
					minDis = temp;
					index = i;
				}
			}
			extractor[index]++;
		}

		return extractor;


	}

	public double[] getAPicBow(Mat pic){
		double[][] picD = this.convertMat(pic);
		return this.getAPicBow(picD);

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

}
