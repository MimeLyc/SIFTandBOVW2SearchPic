package SiftAndBow;


import Jama.Matrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QQQ on 2017/4/16.
 */
public class SpectralClustering implements ClusterInterface {
	private MyMatrix data;
	private int K;
	private double sigma = 1;
	private MyMatrix Wmatrix;
	private MyMatrix Dmatrix;
	private MyMatrix Lmatrix;
//	前K个特征向量组成的特征矩阵
	private MyMatrix EigMatrix;
	private MyMatrix NEigMatrix;
	private HashMap<Integer,List<Integer>> indexMap = new HashMap<>();

	private double[][] centers;


	public SpectralClustering(double[][] data,int k,double sigma){
		this.data = new MyMatrix(data.clone());
		this.K = k;
		this.sigma = sigma;
		System.out.println(data.length);
		this.Wmatrix = new MyMatrix(data.length,data.length);
		this.Dmatrix = new MyMatrix(data.length,data.length);
		this.Lmatrix = new MyMatrix(data.length,data.length);
		this.EigMatrix = new MyMatrix(data.length,K);
		this.NEigMatrix = new MyMatrix(data.length,K);
		this.centers = new double[K][data[0].length];

	}

	public void setData(double[][] data){
		this.data = new MyMatrix(data.clone());
		this.Wmatrix = new MyMatrix(data.length,data.length);
		this.Dmatrix = new MyMatrix(data.length,data.length);
		this.Lmatrix = new MyMatrix(data.length,data.length);
		this.centers = new double[K][data[0].length];
	}

//	相似度计算，高斯相似度函数过小？普通欧氏距离？
	private double culSimilarity(double[] arr1,double[] arr2){
		double result = 0;
		for(int i=0;i<arr1.length;i++){
			result += Math.pow((arr1[i]-arr2[i]),2);
		}
//		System.out.println(result);
//		高斯相似度 TODO
		result = Math.exp(-(result)/(1));
//		result = 1/(1+Math.sqrt(result));
		return result;
	}


	private void culWmatrix(){
		for (int i =0;i<data.rows();i++){
			for(int j=0;j<data.rows();j++){
//TODO
// 	Wmatrix.set(i,j);
				double similar = this.culSimilarity(data.getRow(i),data.getRow(j));
				Wmatrix.set(i,j,similar);
				Wmatrix.set(j,i,similar);

			}

		}


	}

	private void culDmatrix(){

		for (int i=0;i<data.rows();i++){
			Dmatrix.set(i,i,Wmatrix.getRowSums()[i]);
		}

	}

	private void setDiagZero(MyMatrix m){

		for (int i=0;i<m.rows();i++){
			m.set(i,i,0);
		}

	}
//TODO 计算拉普拉斯矩阵 ，公式为L = D-W,L = D^-1/2*L*D^-1/2
	private void culLmatrix(){

		Lmatrix = Dmatrix.substract(Wmatrix);
//		for (int i=0;i<Lmatrix.getData().length;i++){
//			for (int j=0;j<Lmatrix.getData()[0].length;j++){
//				System.out.print(Lmatrix.getData()[i][j]+";");
//			}
//			System.out.println();
//		}

		MyMatrix sqrtDM = this.Dmatrix.clone();
		for (int i=0;i<Dmatrix.rows();i++){
			sqrtDM.set(i,i,1/Math.sqrt(Dmatrix.get(i,i)));
		}




		Lmatrix.setMatrix(Lmatrix.times(Lmatrix));
//		for (int i=0;i<Lmatrix.getData().length;i++){
//			for (int j=0;j<Lmatrix.getData().length;j++){
//				System.out.print(Lmatrix.getData()[i][j]+";");
//			}
//			System.out.println();
//		}
		Lmatrix.setMatrix(Lmatrix.times(sqrtDM));

	}

	private void getKEIG(){
//		特征值对角阵
		MyMatrix eigD = new MyMatrix(Lmatrix.eig().getD().getArray());

//		this.printMatrix(eigD);
//		特征向量矩阵，按列排序
//		System.out.println();
		MyMatrix eigV = new MyMatrix(Lmatrix.eig().getV().getArray());
//		this.printMatrix(eigV);
//	获取特征值
		double[] eigDd = new double[eigD.rows()];
		for (int i = 0;i<eigD.rows();i++){
			eigDd[i] = eigD.get(i,i);
		}


//	排序
		for (int i=0;i<K;i++){
			double tempM = eigDd[0];
			int index = 0;
			EigMatrix.setCols(i,eigV.getCol(0));
			for (int j=0;j<eigDd.length;j++){
				if(eigDd[j]<tempM){
					tempM = eigDd[j];
					EigMatrix.setCols(i,eigV.getCol(j));
					index = j;
				}
			}
			eigDd[index] = eigDd[0]+1;
		}



	}

//	归一化X特征向量矩阵
	private void normalizeEig(){
		MyMatrix dmMatrix =EigMatrix.dotMultiply(EigMatrix);
		double[] colSum = dmMatrix.colSum();
		for (int i=0;i<EigMatrix.rows();i++){
			for (int j=0;j<EigMatrix.cols();j++){
				NEigMatrix.set(i,j,EigMatrix.get(i,j)/Math.sqrt(colSum[j]));
			}
		}


	}


	private HashMap<Integer,List<Integer>> getKmeans(){

		MyKmeans kmeans = new MyKmeans(K,NEigMatrix.getData());
		kmeans.setThreshold(0.01);

		double[][] center = kmeans.cluster(NEigMatrix.getData());
		HashMap<Integer,List<Integer>> result = kmeans.getIndexMap();
		this.indexMap = result;
		return result;

	}

	private void culCenters(){

		for(int i=0;i<indexMap.size();i++){

			centers[i] = this.getCenters(indexMap.get(i));

		}

	}

	private double[] getCenters(List<Integer> list){
		double[] result = new double[data.cols()];
		double[] sum = new double[data.cols()];
		for (int i=0;i<data.cols();i++){
			for (int j =0;j<list.size();j++){
				sum[i] += data.get(list.get(j),i);

			}
		}

		for (int i = 0;i<data.cols();i++){
			result[i] = sum[i]/list.size();
		}

		return result;

	}


	@Override
	public double[][] cluster(double[][] data) {
		this.setData(data);
		this.culWmatrix();


		this.setDiagZero(Wmatrix);
//		this.printMatrix(Wmatrix);
		this.culDmatrix();
//		this.printMatrix(Dmatrix);

		this.culLmatrix();

		System.out.println("L:");
		this.printMatrix(Lmatrix);

		this.getKEIG();

//		this.printMatrix(EigMatrix);

		this.normalizeEig();

		System.out.println("NE:");
		this.printMatrix(NEigMatrix);

		this.getKmeans();
		this.culCenters();

		return this.centers;
	}

	private void printMatrix(MyMatrix m){

		for (int i=0;i<m.rows();i++){
			for (int j = 0;j<m.cols();j++){
				System.out.print(m.get(i,j)+";");
			}
			System.out.println();
		}

	}


	public static void main(String[] args) {
		double[][] test = new double[][]{{0,0},{1,1},{20,20},{21,21},{70,70},{80,80},{500,500},{500,500}};
		SpectralClustering testKm = new SpectralClustering(test,4,1);
		double[][] result = testKm.cluster(test);

//
//		double[][] test = new double[][]{{0,0},{1,1}};
//		MyMatrix m = new MyMatrix(test);
//		for (int i=0;i<m.getArray().length;i++){
//			for (int j=0;j<m.getArray().length;j++){
//				System.out.print(m.times(m).getArray()[i][j]+";");
//			}
//			System.out.println();
//		}


		for (int i=0;i<result.length;i++){
			for (int j=0;j<result[0].length;j++){
				System.out.print(result[i][j]+";");

			}
			System.out.println();
		}

	}
}
