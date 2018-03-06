package SiftAndBow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by QQQ on 2017/4/28.
 */
public class MyKmeansPP extends MyKmeans {

	@Override
	protected void iniRanCentre(){

		Random ran = new Random();
		int tempIndex = ran.nextInt(data.length);
		centres[0] = data[tempIndex].clone();


		for (int i = 1;i<K;i++){
			double[] poss = new double[data.length];

			double allDis2 = 0;
			int maxIndex = 0;
//			计算每个数据的到最近点的距离
			for (int j = 0;j<data.length;j++){
				double dis2 = Math.pow(this.calMinDis(data[j],centres,i),2);
				poss[j] = dis2;
				allDis2 += dis2;
			}

//			计算落入某个区间的概率
			double tempSum = 0;
			for (int j = 0;j<poss.length;j++
				 ) {
				poss[j] = poss[j]/allDis2;
				tempSum += poss[j];
//				System.out.println(tempSum);
				poss[j] = tempSum;
//				System.out.println(pos);
			}

//			产生随机点，根据轮盘算法确定中心点
			double ranPos = ran.nextDouble();
			for (int j = 0;j<poss.length;j++){
				if(ranPos - poss[j]<0){
					System.out.println(ranPos+";"+poss[j]);
					centres[i] = data[j];
					break;
				}

			}

		}

	}

	/**
	 * 计算数据到已有中心点的最近距离。
	 * @param p
	 * @param centres	已计算的中心点
	 * @param k	已计算的中心点个数
	 * @return
	 */
	private double calMinDis(double[] p,double[][] centres,int k){
		double result = calDis(p,centres[0]);

		for (int i=1;i<k;i++){
			double tempD = calDis(p,centres[i]);
			if (tempD<result)
				result = tempD;
		}

		return result;

	}

	public static void main(String[] args){
		double[][] test = new double[][]{{0,0},{1,1},{20,20},{21,21},{70,70},{80,80},{500,501},{500,500}};
		MyKmeansPP testKm = new MyKmeansPP();
		testKm.setK(4);
		testKm.cluster(test);
		HashMap<Integer,List<Integer>> index = testKm.getIndexMap();
		for (Map.Entry<Integer,List<Integer>> i:index.entrySet()
				) {
			System.out.println(i.getKey()+":");
			for (int j=0;j<i.getValue().size();j++){
				System.out.println(i.getValue().get(j)+";");
			}
			System.out.println();

		}
//		for(int i=0;i<data.length;i++){
//			for(int j=0;j<data[0].length;j++){
//				System.out.print(data[i][j]+";");
//			}
//			System.out.println();
//		}

	}


}
