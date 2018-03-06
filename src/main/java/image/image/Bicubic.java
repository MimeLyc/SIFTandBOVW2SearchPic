package image.image;

public class Bicubic {

	private static double a1, b1, c1, d1;
	private static double a2, b2, c2, d2;
	private static double a3, b3, c3, d3;
	private static double a4, b4, c4, d4;
	private static double[][] BP = new double[4][4];;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static int[][] resize(int image[][], int newW, int newH) {
		int[][] result = new int[newH][newW];
		int w = image[0].length;
		int h = image.length;
		double hRatio = (h + 0.0) / newH;
		double wRatio = (w + 0.0) / newW;
		for (int i = 0; i < newH; i++) {
			double row = i * hRatio;
			int intOfRow = (int) Math.floor(row);
			double decimalOfRow = row - intOfRow;
			for (int j = 0; j < newW; j++) {
				double col = j * wRatio;
				int intOfCol = (int) Math.floor(col);
				double decimalOfCol = col - intOfCol;
				if (decimalOfRow == 0 && decimalOfCol == 0) {
					result[i][j] = image[intOfRow][intOfCol];
				} else {
					getBicubicPanel(image, intOfRow, intOfCol);
					updateC();
					result[i][j]=getBicubicValue(decimalOfRow,decimalOfCol);
				}
			}
		}
		return result;
	}

	public static void getBicubicPanel(int image[][], int row, int col) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				int newRow = row - 1 + i;
				int newCol = col - 1 + j;
				if (newCol >= image[0].length) {
					newCol = image[0].length - 1;
				}
				if (newCol < 0) {
					newCol = 0;
				}
				if (newRow >= image.length) {
					newRow = image.length - 1;
				}
				if (newRow < 0) {
					newRow = 0;
				}
				BP[i][j] = image[newRow][newCol];
			}
		}
	}

	public static void updateC() {
		a1 = BP[1][1];
		b1 = -.5 * BP[1][0] + .5 * BP[1][2];
		c1 = BP[1][0] - 2.5 * BP[1][1] + 2 * BP[1][2] - .5 * BP[1][3];
		d1 = -.5 * BP[1][0] + 1.5 * BP[1][1] - 1.5 * BP[1][2] + .5 * BP[1][3];
		a2 = -.5 * BP[0][1] + .5 * BP[2][1];
		b2 = .25 * BP[0][0] - .25 * BP[0][2] - .25 * BP[2][0] + .25 * BP[2][2];
		c2 = -.5 * BP[0][0] + 1.25 * BP[0][1] - BP[0][2] + .25 * BP[0][3] + .5 * BP[2][0] - 1.25 * BP[2][1] + BP[2][2]
				- .25 * BP[2][3];
		d2 = .25 * BP[0][0] - .75 * BP[0][1] + .75 * BP[0][2] - .25 * BP[0][3] - .25 * BP[2][0] + .75 * BP[2][1]
				- .75 * BP[2][2] + .25 * BP[2][3];
		a3 = BP[0][1] - 2.5 * BP[1][1] + 2 * BP[2][1] - .5 * BP[3][1];
		b3 = -.5 * BP[0][0] + .5 * BP[0][2] + 1.25 * BP[1][0] - 1.25 * BP[1][2] - BP[2][0] + BP[2][2] + .25 * BP[3][0]
				- .25 * BP[3][2];
		c3 = BP[0][0] - 2.5 * BP[0][1] + 2 * BP[0][2] - .5 * BP[0][3] - 2.5 * BP[1][0] + 6.25 * BP[1][1] - 5 * BP[1][2]
				+ 1.25 * BP[1][3] + 2 * BP[2][0] - 5 * BP[2][1] + 4 * BP[2][2] - BP[2][3] - .5 * BP[3][0]
				+ 1.25 * BP[3][1] - BP[3][2] + .25 * BP[3][3];
		d3 = -.5 * BP[0][0] + 1.5 * BP[0][1] - 1.5 * BP[0][2] + .5 * BP[0][3] + 1.25 * BP[1][0] - 3.75 * BP[1][1]
				+ 3.75 * BP[1][2] - 1.25 * BP[1][3] - BP[2][0] + 3 * BP[2][1] - 3 * BP[2][2] + BP[2][3] + .25 * BP[3][0]
				- .75 * BP[3][1] + .75 * BP[3][2] - .25 * BP[3][3];
		a4 = -.5 * BP[0][1] + 1.5 * BP[1][1] - 1.5 * BP[2][1] + .5 * BP[3][1];
		b4 = .25 * BP[0][0] - .25 * BP[0][2] - .75 * BP[1][0] + .75 * BP[1][2] + .75 * BP[2][0] - .75 * BP[2][2]
				- .25 * BP[3][0] + .25 * BP[3][2];
		c4 = -.5 * BP[0][0] + 1.25 * BP[0][1] - BP[0][2] + .25 * BP[0][3] + 1.5 * BP[1][0] - 3.75 * BP[1][1]
				+ 3 * BP[1][2] - .75 * BP[1][3] - 1.5 * BP[2][0] + 3.75 * BP[2][1] - 3 * BP[2][2] + .75 * BP[2][3]
				+ .5 * BP[3][0] - 1.25 * BP[3][1] + BP[3][2] - .25 * BP[3][3];
		d4 = .25 * BP[0][0] - .75 * BP[0][1] + .75 * BP[0][2] - .25 * BP[0][3] - .75 * BP[1][0] + 2.25 * BP[1][1]
				- 2.25 * BP[1][2] + .75 * BP[1][3] + .75 * BP[2][0] - 2.25 * BP[2][1] + 2.25 * BP[2][2] - .75 * BP[2][3]
				- .25 * BP[3][0] + .75 * BP[3][1] - .75 * BP[3][2] + .25 * BP[3][3];

	}

	public static int getBicubicValue(double dr, double dc) {
		double dr2 = dr * dr;
		double dr3 =dr2 * dr;
		double dc2 = dc * dc;
		double dc3 = dc2 * dc;

		double temp= (a1 + b1 * dc + c1 * dc2 + d1 * dc3) + (a2 + b2 * dc + c2 * dc2 + d2 * dc3) * dr
				+ (a3 + b3 * dc + c3 * dc2 + d3 * dc3) * dr2 + (a4 + b4 * dc + c4 *dc2 + d4 *dc3) * dr3;
		int result=(int)temp;
		if(result<0){
			result=0;
		}
		if(result>255){
			result=255;
		}
		return result;
	}
}
