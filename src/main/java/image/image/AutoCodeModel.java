package image.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.datavec.image.loader.NativeImageLoader;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.NetSaverLoaderUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;

import data.DatabaseController;

public class AutoCodeModel {
	protected static int height = 100;
	protected static int width = 100;
	private static  MultiLayerNetwork network;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 String basePath="D:/data/";
		  loadNetWork(basePath);

		 
 
	        String pathOfClass=basePath+"data_x/data_Fdress";
	        File ClassPackets=new File(pathOfClass);
	        String[] listOfImageName=ClassPackets.list();
	        ImageKey[]listOfImage=new ImageKey[listOfImageName.length];
	        int number=0;
	     for(String imageFileName:listOfImageName){
	    	 ImageKey ik=new ImageKey();
	    	String pathOfImageFile=pathOfClass+"/"+imageFileName;
	    	ik.setPath(pathOfImageFile);
	    	ik.setC(imageFileName.split("_")[0]);
	    	ik.setImageKey(INDArrayToArray(getDescriptor(getImage1d(pathOfImageFile,height,width,1),4)));
	    	listOfImage[number]=ik;
	    	number++;
	     }
	     DatabaseController dc=new DatabaseController();
	     dc.setConn();
        dc.saveImageKey(listOfImage);

	}
	
public static void loadNetWork(String basePath)throws IOException {
//	String confFile = new String(Files.readAllBytes(Paths.get(basePath+"org.deeplearning4j.nn.multilayer.MultiLayerNetwork@6d654064-conf.json")));
//	MultiLayerNetwork network=NetSaverLoaderUtils.loadNetworkAndParameters(confFile,basePath+"org.deeplearning4j.nn.multilayer.MultiLayerNetwork@6d654064.bin");
//	network.setUpdater(NetSaverLoaderUtils.loadUpdators(basePath+"org.deeplearning4j.nn.multilayer.MultiLayerNetwork@6d654064updators.bin"));
// System.out.print("loading end");
	
 String confFile = new String(Files.readAllBytes(Paths.get(basePath+"8.json")));
	 network=NetSaverLoaderUtils.loadNetworkAndParameters(confFile,basePath+"7.bin");
	network.setUpdater(NetSaverLoaderUtils.loadUpdators(basePath+"9.bin"));
System.out.print("loading end");

}
public static INDArray getImage(String path) throws IOException{
	 NativeImageLoader nil=new NativeImageLoader(height,width, 3);
	 ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
	 INDArray img=nil.asMatrix(new File(path));
	 scaler.preProcess(img);
	 return img;
}
public static INDArray getImage1d(String path,int h,int w,int channels) throws IOException{
  	 NativeImageLoader nil=new NativeImageLoader(h,w, channels);
  	 ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
  	 INDArray img=nil.asMatrix(new File(path));

  	 scaler.preProcess(img);
	INDArray result= Nd4j.create( 1, h*w);
	for(int i=0;i<h;i++){
		for(int j=0;j<w;j++){
			Double a=img.getDouble(0,0,i,j);
			result.putScalar(0, i*h+j,a);
		}
	}
  	 return result;
  }	
	
public static INDArray getDescriptor( INDArray img,int n){
	INDArray result=img;
	for(int i=0;i<=n;i++){
		result=network.activationFromPrevLayer(i, result, false);
	}
	return result;
}
public static void getTime(){
	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 System.out.println(df.format(new Date()));
}
public static double getDistance(INDArray des1,INDArray des2){
	double result=0;
	for(int i=0;i<des1.length();i++){
		result=result+Math.pow(des1.getDouble(i)-des2.getDouble(i),2);
	}
	return Math.pow(result, 0.5);
}
public static double getDistance(INDArray des1,double[] des2){
	double result=0;
	for(int i=0;i<des1.length();i++){
		result=result+Math.pow(des1.getDouble(i)-des2[i],2);
	}
	return Math.pow(result, 0.5);
}
public static double[] INDArrayToArray(INDArray ik){
	double[] result=new double[ik.length()];
	for(int i=0;i<ik.length();i++){
		result[i]=ik.getDouble(i);
	}
	return result;
}
}
