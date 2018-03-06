package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import dataService.HashCodeService;
import image.image.ImageHash;

public class HashCodeServiceImp implements HashCodeService{

	@Override
	public void saveHashCodeToSQL(ArrayList<ImageHash> hashCodeList) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		DatabaseController dc=new DatabaseController();
		dc.setConn();
		Statement stmt=dc.getStmt();
	
		int count=0;
		ImageHash imageHash=null;
		for(int i=0;i<hashCodeList.size();i++){
			imageHash=hashCodeList.get(i);
			String path=imageHash.getPath();
			path=path.replaceAll("\\\\", "\\\\\\\\");
			String sql="insert into imagehash values('"+path+"',"+imageHash.getHash()+",'"+imageHash.getC()+"');";
			
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
			if(count==1000){
				dc.commit();
				count=0;
			}
		}
		dc.commit();
		dc.close();
	
	}

	@Override
	public ArrayList<ImageHash> getHashCode() {
		// TODO Auto-generated method stub
		DatabaseController dc=new DatabaseController();
		dc.setConn();
		Statement stmt=dc.getStmt();
		ArrayList<ImageHash> HashCodeList=new ArrayList<ImageHash>();
		String sql="select * from imagehash";
		
		try {
			ResultSet rs=stmt.executeQuery(sql);
		
			while(rs.next()){
				ImageHash ih=new ImageHash();
				
				ih.setPath(rs.getString(1));
				ih.setHash(rs.getLong(2));
				ih.setC(rs.getString(3));
				HashCodeList.add(ih);
			}
			rs.close();
			dc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return HashCodeList;
	}

}
