package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import dataService.Dirservice;
import dataService.WordService;
import image.image.ImageKey;

public class DatabaseController {
	private Connection conn;
	private Statement stmt;
	private static ComboPooledDataSource cp;
	static {

		// try{
		// Class.forName("com.mysql.jdbc.Driver");
		// }catch(ClassNotFoundException e){
		// e.printStackTrace();
		// }
		// String url="jdbc:mysql://localhost:3306/imagesearch"; //JDBC的URL
		// try {
		// conn = DriverManager.getConnection(url,"root",Path.secret);
		//
		// conn.setAutoCommit(false);
		// stmt=conn.createStatement();
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			cp = new ComboPooledDataSource();
			cp.setJdbcUrl("jdbc:mysql://localhost:3306/imagesearch");
			cp.setDriverClass("com.mysql.jdbc.Driver");
			cp.setUser("root");
			cp.setPassword(Path.secret);
			cp.setMaxPoolSize(20);
			cp.setMinPoolSize(5);
			cp.setAcquireIncrement(5);
			cp.setInitialPoolSize(5);
			cp.setMaxIdleTime(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Dirservice d = new DirServiceImp();
		WordService w = new WordServiceImp();
		// HashMap<String, double[]> words=w.getWords();
		d.saveDirToSQL(new double[][] { { 1, 1, 1 } });
		HashMap<String, double[]> words = new HashMap<String, double[]>();
		words.put("d:d2\\d", new double[] { 12, 21, 1 });
		w.saveWordToSQL(words);
	}

	public void setConn() {
		try {
			conn = cp.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Statement getStmt() {
		return stmt;
	}

	public void commit() {
		try {
			conn.commit();
		} catch (Throwable e1) {
			if (conn != null) {
				try {
					conn.rollback(); // 数据库回滚
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			throw new RuntimeException(e1);
		}

	}

	public void saveImageKey(ImageKey[] list) {
		try {
			for (int i = 0; i < list.length; i++) {
				stmt.executeUpdate(makeSql(list[i], "imagekeyT"));
				if (i % 100 == 0) {
					conn.commit();
				}
			}

			conn.commit();
		} catch (Throwable e1) {
			if (conn != null) {
				try {
					conn.rollback(); // 数据库回滚
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}

			throw new RuntimeException(e1);
		}

	}

	public ArrayList<ImageKey> getImageKey(String tableName) {
		ArrayList<ImageKey> ikList = new ArrayList<ImageKey>();

		String sql = "select * from " + tableName;
		try {
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				ImageKey ik = new ImageKey();
				double[] imageKey = new double[30];
				ik.setPath(rs.getString(1));
				ik.setC(rs.getString(2));
				for (int i = 0; i < 30; i++) {
					imageKey[i] = rs.getDouble(i + 3);
				}
				ik.setImageKey(imageKey);
				ikList.add(ik);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ikList;
	}

	public void close() {
		if (stmt != null) {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private static String makeSql(ImageKey ik, String tableName) {
		String result = "insert into " + tableName + " values(";
		result = result + "'" + ik.getPath() + "' ," + ik.getC();
		double[] imageKey = ik.getImageKey();
		for (int i = 0; i < imageKey.length; i++) {
			result = result + "," + imageKey[i];
		}
		result = result + ");";

		return result;
	}
}
