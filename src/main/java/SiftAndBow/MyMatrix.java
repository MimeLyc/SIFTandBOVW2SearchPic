package SiftAndBow;

import Jama.Matrix;

/**
 * Created by QQQ on 2017/4/16.
 */
public class MyMatrix extends Matrix{
	private double[][] data;
	private int rows;
	private int cols;

	public void transRows(int i,int j){
		double[] temp = data[i].clone();
		data[i] = data[j].clone();
		data[j] = temp;
	}

	public void transCols(int i,int j){
		double[] temp = new double[rows];
		temp = this.getCol(i).clone();
		this.setCols(i,this.getCol(j).clone());
		this.setCols(j,temp.clone());

	}

	public MyMatrix setMatrix(Matrix m){

		this.data = m.getArray();
		return new MyMatrix(this.data);
	}

	public MyMatrix(double[][] data){

		super(data);
		this.data = super.getArray();
		this.rows = data.length;
		this.cols = data[0].length;

	}

	public MyMatrix(int rows,int cols){
		super(rows,cols);
		this.data = super.getArray();
		this.rows = rows;
		this.cols = cols;
	}


	public MyMatrix substract(MyMatrix m){
		MyMatrix result =new MyMatrix(rows(),cols);
		for (int i=0;i<rows;i++){
			for (int j=0;j<cols;j++){
				result.set(i,j,data[i][j]-m.get(i,j));
//				System.out.println(result.get(i,j));
			}
		}
		return result;


	}


	public double[][] getData(){
		return this.data;
	}

	public double get(int i,int j){
		return data[i][j];
	}

	public void set(int i,int j,double num){
		data[i][j] = num;
	}

	public int rows(){
		return this.rows;

	}

	public int cols(){

		return this.cols;

	}

	public double[] getRowSums(){
		double[] sums = new double[rows];
		for (int i=0;i<rows;i++){
			for (int j=0;j<cols;j++){
				sums[i]+= data[i][j];
			}
		}
		return sums;
	}

	public MyMatrix clone(){

		MyMatrix copyM = new MyMatrix(this.data);
		return copyM;


	}

	public void dotSqrt(){

		for (int i=0 ; i<rows;i++){
			for (int j = 0;j<cols;j++){
				data[i][j] = Math.sqrt(data[i][j]);
			}
		}

	}
//	矩阵的点乘
	public MyMatrix dotMultiply(MyMatrix m){

		MyMatrix result = new MyMatrix(rows,cols);
		for (int i=0;i<rows;i++){
			for (int j=0;j<cols;j++){
				result.set(i,j,data[i][j]*m.get(i,j));
			}
		}

		return result;

	}


//TODO
	public MyMatrix multiply(MyMatrix m){
		int rowN = rows;
		int colN = m.cols();
		MyMatrix result = new MyMatrix(rows,m.cols());
		for (int i=0;i<rowN;i++){
			for (int j=0;j<colN;j++){
				result.set(i,j,this.dArrayMultiply(data[i],m.getCol(j)));
			}
		}
		return result;

	}

	public double[] colSum(){
		double[] result = new double[cols];
		for (int i=0;i<cols;i++){
			result[i]=0;
			for (int j=0;j<rows;j++){
				result[i] += data[j][i];
			}
		}
		return result;

	}

	public double dArrayMultiply(double[] arr1,double[] arr2){

		double result = 0;
		for (int i=0;i<arr1.length;i++){
			result += arr1[i]*arr2[i];
		}
		return result;

	}

	public void setCols(int i,double[] col){
		for (int j = 0;j<rows;j++){
			data[j][i] = col[j];
		}
	}

	public void setRows(int i,double[] row){

		this.data[i] = row.clone();

	}

	public double[] getRow(int i){
		return data[i];
	}

	public double[] getCol(int j){
		double[] col = new double[rows];
		for (int i=0;i<rows;i++
			 ) {

			col[i] = data[i][j];
		}
		return col;
	}

}
