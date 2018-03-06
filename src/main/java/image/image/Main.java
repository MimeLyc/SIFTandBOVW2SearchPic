package image.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.nd4j.linalg.api.ndarray.INDArray;

import SiftAndBow.ClusterPic;
import SiftAndBow.DataSaving;
import SiftAndBow.ImgSearch;
import SiftAndBow.OutInterface;
import data.DatabaseController;
import data.DirServiceImp;
import data.HashCodeServiceImp;
import data.WordServiceImp;
import dataService.Dirservice;
import dataService.HashCodeService;
import dataService.WordService;

public class Main {
	private static ArrayList<ImageKey> ikList;
	private static String CForTest="";
	private static HashMap<Long,ArrayList<ImageHash>> pHash1=new HashMap<Long,ArrayList<ImageHash>>();
	private static HashMap<Long,ArrayList<ImageHash>> pHash2=new HashMap<Long,ArrayList<ImageHash>>();
	private static HashMap<Long,ArrayList<ImageHash>> pHash3=new HashMap<Long,ArrayList<ImageHash>>();
	private static HashMap<Long,ArrayList<ImageHash>> pHash4=new HashMap<Long,ArrayList<ImageHash>>();
	private static ArrayList<ImageHash> imageHashList;
	private static KDTree kt;
	private static double[][] dic ;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

//		DatabaseController dc=new DatabaseController();
//		dc.setConn();
//		ikList = dc.getImageKey("imagekey");
//		dc.close();
//		System.out.println(1);
//		KDTree kt=KDTree.build(ikList);
//		System.out.println(1);
//		ImageKey ik=ikList.get(111);
//		System.out.println(ik.getPath());
//	System.out.println(kt.queryP(ik.getImageKey(),10));
//	

//		DataSaving dt = new DataSaving();
//		dt.saveDir("D:\\data\\oitrain",30);
//		long startP = System.nanoTime();
//		dt.saveWords("D:\\data\\data_x\\data_Fdress");
//		long endP = System.nanoTime();
//		double msP = (endP - startP) / 1000000d;
//		System.out.printf("Saving all picture words cost %,.3f ms%n",msP);
		loadSift();
		loadKDTree();
	double[] input=getSift("D:\\data\\data_x\\data_Fdress\\130_37.jpg");
	long startP = System.nanoTime();
	for(int i=0;i<1000;i++){
		kt.queryP(input,5);
	}
	long endP = System.nanoTime();
	double msP = (endP - startP) / 1000000d;
	System.out.println(msP);
	
//		
//		loadHash();
//		
//		fastHash("D:\\data\\data_x\\data_Fdress\\126_11.jpg");
//		System.out.println(0);
//		fastHash("D:\\data\\data_x\\data_Fdress\\126_10.jpg");
//		System.out.println(0);
//		fastHash("D:\\data\\data_x\\data_Fdress\\126_12.jpg");
//		System.out.println(0);
//		fastHash("D:\\data\\data_x\\data_Fdress\\126_13.jpg");
	}
public static void loadSift(){
	
	Dirservice ds = new DirServiceImp();
	dic = ds.getDir();
	WordService ws=new WordServiceImp();
	ikList=ws.getWordsArray();
	
	
}
public static void loadKDTree(){
	kt=KDTree.build(ikList);
}
public static double[] getSift(String path){
	ClusterPic cp = new ClusterPic();
	cp.setDictionary(dic);
	try {
		return cp.getPicWord(path).get(path);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
public static void loadHash(){
	HashCodeService hcs=new HashCodeServiceImp();
	imageHashList = hcs.getHashCode();
	for(int i=0;i<imageHashList.size();i++){
		ImageHash ih=imageHashList.get(i);
		long h1=ih.getHash()&0xffff000000000000l;
		long h2=ih.getHash()&0x0000ffff00000000l;
		long h3=ih.getHash()&0x00000000ffff0000l;
		long h4=ih.getHash()&0x000000000000ffffl;
		loadOne(h1,pHash1,ih);
		loadOne(h2,pHash2,ih);
		loadOne(h3,pHash3,ih);
		loadOne(h4,pHash4,ih);
	}
}
public static void fastHash(String path ){
	long hashCode=Phash.pHashl(path);
	ArrayList<ImageForSort> ifsList=new ArrayList<ImageForSort>();
	HashMap<String ,ImageForSort> ifsHash=new HashMap<String ,ImageForSort>();
	long h1=hashCode&0xffff000000000000l;
	long h2=hashCode&0x0000ffff00000000l;
	long h3=hashCode&0x00000000ffff0000l;
	long h4=hashCode&0x000000000000ffffl;
	if(pHash1.get(h1)!=null){
		ArrayList<ImageHash> ihl=pHash1.get(h1);
		makeSort(ihl, hashCode, ifsHash);
	}
	if(pHash2.get(h2)!=null){
		ArrayList<ImageHash> ihl=pHash2.get(h2);
		makeSort(ihl, hashCode, ifsHash);
	}
	if(pHash3.get(h3)!=null){
		ArrayList<ImageHash> ihl=pHash3.get(h3);
		makeSort(ihl, hashCode, ifsHash);
	}
	if(pHash4.get(h4)!=null){
		ArrayList<ImageHash> ihl=pHash4.get(h4);
		makeSort(ihl, hashCode, ifsHash);	
	}
	Iterator<?> iter = ifsHash.entrySet().iterator();
	while(iter.hasNext()){
		Map.Entry entry = (Map.Entry) iter.next();
		ifsList.add((ImageForSort)entry.getValue());
	}
	 Comparator<ImageForSort> myCom=new Comparator<ImageForSort>(){

			@Override
			public int compare(ImageForSort arg0, ImageForSort arg1) {
				// TODO Auto-generated method stub
				if(arg0.getDistance()==arg1.getDistance())
				return 0;
				else if(arg0.getDistance()>arg1.getDistance()){
					return 1;
				}
				else return -1;
			}
	    	 
	     };
	     Collections.sort(ifsList,myCom );
	     for(int i=0;i<ifsList.size();i++){
	System.out.println(ifsList.get(i).getPath()+" "+ifsList.get(i).getDistance());
	}
}
public static void makeSort(ArrayList<ImageHash> ihl,long hashCode,HashMap<String ,ImageForSort> ifsHash){
	for(int i=0;i<ihl.size();i++){
		ImageHash ih=ihl.get(i);
		ImageForSort ims=new ImageForSort();
		ims.setC(ih.getC());
		ims.setPath(ih.getPath());
		ims.setDistance(Phash.getWeight(hashCode^ih.getHash()));
		ifsHash.put(ims.getPath(), ims);
	}
}
public static void loadOne(long h,HashMap<Long,ArrayList<ImageHash>> pHash,ImageHash ih){
	if(pHash.get(h)==null){
		ArrayList<ImageHash> ihl=new ArrayList<ImageHash>();
		ihl.add(ih);
		pHash.put(h,ihl);
	}else{
		ArrayList<ImageHash> ihl=pHash.get(h);
		ihl.add(ih);
		pHash.put(h,ihl);
	}
}
	public static boolean searchImage(INDArray descriptor) {
		boolean result=true;
		ImageForSort[] listOfImage = new ImageForSort[ikList.size()];
		int number=0;
		for(ImageKey ik:ikList){
			ImageForSort ifs=new ImageForSort();
			ifs.setC(ik.getC());
			ifs.setPath(ik.getPath());
			ifs.setDistance(AutoCodeModel.getDistance(descriptor, ik.getImageKey()));
			listOfImage[number]=ifs;
			number++;
		}
		  Comparator<ImageForSort> myCom=new Comparator<ImageForSort>(){

				@Override
				public int compare(ImageForSort arg0, ImageForSort arg1) {
					// TODO Auto-generated method stub
					if(arg0.getDistance()==arg1.getDistance())
					return 0;
					else if(arg0.getDistance()>arg1.getDistance()){
						return 1;
					}
					else return -1;
				}
		    	 
		     };
		     Arrays.sort(listOfImage,myCom );
		result =judge(listOfImage);
		return result;
	}
	public static boolean judge(ImageForSort[] listOfImage){
		boolean result=true;
		ImageForSort top1=listOfImage[0];
		ImageForSort top2=listOfImage[1];
		ImageForSort top3=listOfImage[2];
		ImageForSort top4=listOfImage[3];
		ImageForSort top5=listOfImage[4];
		if(top1.getDistance()==0.0){
			CForTest=top1.getC();
		}else if((top1.getC().equals(top2.getC()))&&(top2.getC().equals(top3.getC()))&&(top3.getC().equals(top4.getC()))&&(top4.getC().equals(top5.getC()))){
			CForTest=top1.getC();
		}
		else{
			result=false;
		}
		return result;
	}
}
