package image.image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Makefile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file=new File("D:/data/data_x");
		 File[] listOfPacket=file.listFiles();
	
		 for(File f:listOfPacket){
			 if(f.isDirectory()){
				 File[] listOfImage=f.listFiles();
				 String directoryName=f.getName();
				 String currentFirst="";
				 String imageFirst="";
				 String imageName="";
				 File fileNew=null;
				 for(File image:listOfImage){
					 imageName=image.getName();
					 imageFirst=imageName.split("_")[0];
					 if(!currentFirst.equals(imageFirst)){
							fileNew=new File("D:/data/data_y/"+directoryName+"/"+directoryName+"_"+imageFirst);
							currentFirst=imageFirst;	
					 }
					 if(!fileNew.exists()){
						 fileNew.mkdirs();
					 }
					try {
						Files.copy(image.toPath(), fileNew.toPath().resolve(image.getName()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 
			 }
		 }
	}

}
