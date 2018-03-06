package image.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageReader {
	public static void main(String[] args) throws IOException {
//		String a = pHash("D:\\data\\data_x\\data_baby\\167_3.jpg");
//		String b = pHash("D:\\data\\data_x\\data_baby\\167_4.jpg");
//		int count = 0;
//		for (int i = 0; i < 64; i++) {
//			if (a.charAt(i) != b.charAt(i)) {
//				count++;
//			}
//		}
//		System.out.println(count);
		int[][]image=readImage("D:\\data\\data_x\\data_baby\\167_4.jpg");
		int[][]newImage= Bicubic.resize(image, 32, 32);
		saveImge(image,"D:\\data\\167_a.jpg");
		saveImge(newImage,"D:\\data\\167_b.jpg");
	}

	public static int[][] readImage(String path) throws IOException {
		BufferedImage img = ImageIO.read(new File(path));
		int w = img.getWidth();
		int h = img.getHeight();
		int startX = 0;
		int startY = 0;
		int offset = 0;
		int scansize = w;
		int dd = w - startX;
		int hh = h - startY;
		int x0 = w / 2;
		int y0 = h / 2;
		int[] rgbArray = new int[offset + hh * scansize + dd];
		int[][] newArray = new int[h][w];
		img.getRGB(startX, startY, w, h, rgbArray, offset, scansize);
		Color c;
		double sum = 0;
		double sum2 = 0;
		for (int i = 0; i < h - startY; i++) {
			for (int j = 0; j < w - startX; j++) {
				c = new Color(rgbArray[i * dd + j]);
				// 彩色图像转换成无彩色的灰度图像Y=0.299*R + 0.587*G + 0.114*B	
				newArray[i][j]= (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());		
			}
		}
		return newArray;
	}

	public static String pHash(String path) {
		int[][] image = null;
		try {
			image = readImage(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[][] newImage = Bicubic.resize(image, 32, 32);
		double[][] dct = DCTTransformation.dct(newImage);
		double mean = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				mean = mean + dct[i][j];
			}
		}
		mean = mean / 64;
		String l = "";
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (dct[i][j] >= mean) {

					l = l + 1;
				} else {
					l = l + 0;
				}
			}
		}
		return l;
	}
	public static  void saveImge(int[][] data,String path){
		 OutputStream output = null;  
		int h=data.length;int w=data[0].length;
		int []newArray=new int[w*h];
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				int gray=data[i][j];
				newArray[i*w+j]=new Color(gray, gray, gray).getRGB();  
			}
			
		}
		File out = new File(path);  
		      
					try {
					    if (!out.exists())
						out.createNewFile();
						  output = new FileOutputStream(out);  
						  BufferedImage imgOut = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);  
						            imgOut.setRGB(0, 0, w, h, newArray, 0, w);  
						           ImageIO.write(imgOut, "jpg", output);  

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  finally {  
						            if (output != null)  
							               try {  
							                    output.close();  
							               } catch (IOException e) {  
						                }  
							       }  

		         

	}
}
