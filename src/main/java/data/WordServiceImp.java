package data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dataService.WordService;
import image.image.ImageKey;

public class WordServiceImp implements WordService{

	@Override
	public void saveWordToSQL(HashMap<String, double[]> words) {
		// TODO Auto-generated method stub
		DatabaseController dc=new DatabaseController();
		dc.setConn();
		Statement stmt=dc.getStmt();
		Iterator<?> iter = words.entrySet().iterator();
		int count=0;
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String key=(String) entry.getKey();
			key=key.replaceAll("\\\\", "\\\\\\\\");
			String sql="insert into words values('"+key+"','";
			double[] val = (double[]) entry.getValue();
			for(int i=0;i<val.length;i++){
				sql=sql+val[i]+",";
			}
			sql=sql+"');";
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
	public HashMap<String, double[]> getWords() {
		// TODO Auto-generated method stub
		DatabaseController dc=new DatabaseController();
		dc.setConn();
		Statement stmt=dc.getStmt();
		HashMap<String, double[]> words=new HashMap<String, double[]>();
		String sql="select * from words";
		
		try {
			ResultSet rs=stmt.executeQuery(sql);
		
			while(rs.next()){
				String path=rs.getString(1);
				String[] str=rs.getString(2).split(",");
				double[] word=new double[str.length];
				for(int i=0;i<str.length;i++){
					word[i]=Double.valueOf(str[i]);
				}
				words.put(path, word);
			}
			rs.close();
			dc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words;
	}

	@Override
	public ArrayList<ImageKey> getWordsArray() {
		// TODO Auto-generated method stub
		DatabaseController dc=new DatabaseController();
		dc.setConn();
		Statement stmt=dc.getStmt();
		ArrayList<ImageKey> words=new ArrayList<ImageKey>();
		String sql="select * from words";
		
		try {
			ResultSet rs=stmt.executeQuery(sql);
		
			while(rs.next()){
				ImageKey ik=new ImageKey();
				ik.setPath(rs.getString(1));
				String[] str=rs.getString(2).split(",");
				double[] word=new double[str.length];
				for(int i=0;i<str.length;i++){
					word[i]=Double.valueOf(str[i]);
				}
				ik.setImageKey(word);
				words.add(ik);
			}
			rs.close();
			dc.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words;
	}

}
