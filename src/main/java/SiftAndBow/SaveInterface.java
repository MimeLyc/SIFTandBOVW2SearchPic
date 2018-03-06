package SiftAndBow;

import java.util.HashMap;

/**
 * Created by QQQ on 2017/4/23.
 */
public interface SaveInterface {

	/**
	 * 保存词典
	 * @param dir 词典
	 */
	public void saveDirToSQL(double[][] dir);

	/**
	 * 保存单词
	 * @param words 图片的单词。String为图片路径（绝对路径，包括所在文件夹）；
	 */
	public void saveWordToSQL(HashMap<String,double[]> words);

}
