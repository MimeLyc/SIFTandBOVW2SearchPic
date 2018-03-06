package dataService;

public interface Dirservice {
	/**
	 * 保存词典
	 * @param dir 词典
	 */
	public void saveDirToSQL(double[][] dir);
	public double[][]  getDir();
}
