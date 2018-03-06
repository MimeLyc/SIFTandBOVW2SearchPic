package image.image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class MakeTrainingData {
	static Random r=new Random();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("D:/data/data_y/data_lqbz");
		 File[] listOfPacket=file.listFiles();
		 for(File f:listOfPacket){
			 if(f.isDirectory()){
				 File[] listOfImage=f.listFiles();
				 int k=10;
				 int n=listOfImage.length;
				 if(n<k){
					 for(int i=0;i<k-n;i++ ){
						 int rn=random(n);
						 try {
							 
							 String oldName=listOfImage[rn].getName();
							 System.out.println(oldName);
							 String sp[]=oldName.split("\\.");
							 String name=sp[0]+"-"+i+".jpg";
							Files.copy(listOfImage[rn].toPath(), f.toPath().resolve(name));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	 
					 }
				 }else if(n>k){
					 for(int i=0;i<n-k;i++ ){
							 int rn=random(n);
							 try {
								Files.delete(listOfImage[rn].toPath());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								i--;
							}
					 }
				 }
				 
			 }
		 }
	}
public static int random(int n){	
	int result=r.nextInt(n);;
	return result;
	
}
}
