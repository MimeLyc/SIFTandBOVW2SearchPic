package dataService;

import java.util.ArrayList;
import java.util.HashMap;

import image.image.ImageKey;

public interface WordService {
	/**
	 * 保存单词
	 * @param words 图片的单词。String为图片路径（绝对路径，包括所在文件夹）；
	 */
	public void saveWordToSQL(HashMap<String,double[]> words);
	public HashMap<String,double[]>  getWords();
	public ArrayList<ImageKey> getWordsArray();
}
