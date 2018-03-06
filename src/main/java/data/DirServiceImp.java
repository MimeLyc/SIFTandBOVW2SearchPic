package data;

import java.sql.*;
import java.util.HashMap;

import dataService.Dirservice;

public class DirServiceImp implements Dirservice {

	@Override
	public void saveDirToSQL(double[][] dir) {
		// TODO Auto-generated method stub
		DatabaseController dc=new DatabaseController();
		dc.setConn();
		Statement stmt=dc.getStmt();
		for(int i=0;i<dir.length;i++){
			String sql="insert into dir values('";
			for(int j=0;j<dir[0].length;j++){
				sql=sql+dir[i][j]+" ,";
			}
			sql=sql+"');";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dc.commit();
		dc.close();
	}

	@Override
	public double[][] getDir() {
		// TODO Auto-generated method stub
		DatabaseController dc=new DatabaseController();
		dc.setConn();
		Statement stmt=dc.getStmt();
		double[][] dir=null;
		String sql="select * from dir";
		String sql0 = "select count(*) rec from (select * from dir) dd";  
		try {
			ResultSet rs=stmt.executeQuery(sql0);
			int n=0;
			while (rs.next()) {  
				    n = rs.getInt("rec");  
			}
			rs=stmt.executeQuery(sql);
			int i=0;
			while(rs.next()){
				String[] center=rs.getString(1).split(",");
				if(dir==null){
					dir=new double[n][center.length];
				}
				for(int j=0;j<center.length;j++){
					dir[i][j]=Double.valueOf(center[j]);
				}
				i++;
			}
			rs.close();
			dc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dir;
	}

}
