package dataService;

import java.util.ArrayList;

import image.image.ImageHash;

public interface HashCodeService {
	public void saveHashCodeToSQL(ArrayList<ImageHash> hashCodeList);
	public ArrayList<ImageHash>  getHashCode();
}
