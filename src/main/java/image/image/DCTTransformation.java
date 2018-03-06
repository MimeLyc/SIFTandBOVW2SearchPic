package image.image;

public class DCTTransformation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static double[][] dct(int[][] image) {
		int n = image.length;
		double result[][] = new double[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				result[i][j]=image[i][j];
			}
		}
		double[][] coeffM=coeffMatrix(n);
		double[][] coeffMT=T(coeffM);
		
		return Multiply(Multiply(coeffM,result),coeffMT);
	}

	private static double[][] T(double[][] matrix) {
		int n=matrix.length;
		double[][] matrixT=new double[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				matrixT[i][j]=matrix[j][i];
			}
		}
		return matrixT;
	}
	private static double[][] coeffMatrix(int n) {  
		        double[][] coeffM = new double[n][n];  
		        double a = 1.0/Math.sqrt(n);  
		        for(int i=0; i<n; i++) {  
		            coeffM[0][i] = a;  
		        }  
		       for(int i=1; i<n; i++) {  
		           for(int j=0; j<n; j++) {  
		               coeffM[i][j] = Math.sqrt(2.0/n) * Math.cos(i*Math.PI*(j+0.5)/(double)n);  
		           }  
		        }  
		        return coeffM;  
		   }  
private static double[][] Multiply(double[][]a,double[][] b){
	if(a[0].length!=b.length){
		return null;
	}
	double [][] result=new double[a.length][b[0].length];
	for(int i=0;i<a.length;i++){
		for(int j=0;j<b[0].length;j++){
			for(int k=0;k<b.length;k++){
				result[i][j]=result[i][j]+a[i][k]*b[k][j];
			}
		}
	}
	return result;
}
}
