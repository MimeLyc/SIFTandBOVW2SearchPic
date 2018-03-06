package image.image;

public class MaxHeap<AnyType extends Comparable<? super AnyType>> {
	private AnyType[] array;
	private int currentSize;

	public MaxHeap(AnyType[] arr) {
		currentSize = arr.length;
		array = (AnyType[]) new Comparable[currentSize + 1];
		int i = 1;
		for (int j = 0; j < arr.length; j++)
			array[i++] = arr[j];
		buildHeap();
	}
	public MaxHeap(int n) {
		currentSize = 0;
		array = (AnyType[]) new Comparable[n + 1];
		
	}
public int getCurrentSize(){
	return currentSize;
}
	/**
	 * 方法名：buildHeap 说明：从中间开始到结束 不断上滤
	 */
	private void buildHeap() {
		for (int i = currentSize / 2; i <= currentSize; i++)
			percolateUp(i);
	}

	private void percolateUp(int position) {
		AnyType temp = array[position];
		while (position > 1) {
			if (array[position / 2].compareTo(temp) < 0) {
				array[position] = array[position / 2];
				position /= 2;
			} else
				break;
		}
		array[position] = temp;
	}

	public  boolean isEmpty() {
		return currentSize == 0;
	}

	public AnyType deleMax() {
		AnyType max = array[1];
		AnyType temp = array[currentSize--];
		int position = 1;
		int child;
		while (position * 2 <= currentSize) {
			child = 2 * position;
			if (child != currentSize && array[child].compareTo(array[child + 1]) < 0)
				child++;
			if (array[child].compareTo(temp) > 0)
				array[position] = array[child];
			else
				break;
			position = child;
		}
		array[position] = temp;
		return max;
	}
	public AnyType getMax() {
	return array[1];	
	}

	public void insert(AnyType x) {
		if (currentSize == array.length - 1)
//			enlargeArray(2 * currentSize + 1);
			deleMax();
		array[++currentSize] = x;
		percolateUp(currentSize);
	}

//	private void enlargeArray(int capacity) {
//		AnyType[] oldArr = array;
//		AnyType[] newArr = (AnyType[]) new Comparable[capacity];
//		for (int i = 1; i < array.length; i++)
//			newArr[i] = oldArr[i];
//		array = newArr;
//	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= currentSize; i++)
			sb.append(array[i] + " ");
		return new String(sb);
	}

	public static void main(String[] args) {
		MaxHeap m=new MaxHeap(20);
	for(int i=0;i<40;i++){
		m.insert(i);
		System.out.println(m.getMax());
	}
		System.out.println(m);
	}

}
