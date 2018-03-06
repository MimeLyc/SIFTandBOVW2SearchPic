package image.image;
public class ImageForSort  extends ImageVO implements Comparable<ImageForSort> {
private double distance;

public double getDistance() {
	return distance;
}
public void setDistance(double distance) {
	this.distance = distance;
}
@Override
public int compareTo(ImageForSort newImage) {
	// TODO Auto-generated method stub
	if(this.distance==newImage.distance)
	return 0;
	else if(this.getDistance()>newImage.getDistance()){
		return 1;
	}else{
		return -1;
	}
}

}
