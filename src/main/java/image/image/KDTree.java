package image.image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Queue;
public class KDTree {
	 private Node kdtree;
	 private class Node{
	        //分割的维度
	        int partitionDimention;
	        //分割的值
	        double partitionValue;
	        //如果为非叶子节点，该属性为空
	        //否则为数据
	      ArrayList<String> path=new ArrayList<String>();
	        //如果为非叶子节点，该属性为空
	        //否则为数据
	        double[] value;
	        //是否为叶子
	        boolean isLeaf=false;
	        //左树
	        Node left;
	        //右树
	        Node right;
	        //每个维度的最小值
	        double[] min;
	        //每个维度的最大值
	        double[] max;
	    }
	 private class NodeP{
		 Node node;
		 double p;
		 private NodeP(Node node,double p){
			 this.node=node;
			 this.p=p;
		 }
	 }
	    private static class UtilZ{
	        /**
	         * 计算给定维度的方差
	         * @param data 数据
	         * @param dimention 维度
	         * @return 方差
	         */
	        static double variance(ArrayList<ImageKey> data,int dimention){
	            double vsum = 0;
	            double sum = 0;
	            for(ImageKey image:data){
	            	double[] d=image.getImageKey();
	                sum+=d[dimention];
	                vsum+=d[dimention]*d[dimention];
	            }
	            int n = data.size();
	            return vsum/n-Math.pow(sum/n, 2);
	        }
	        /**
	         * 取排序后的中间位置数值
	         * @param data 数据
	         * @param dimention 维度
	         * @return
	         */
	        static double median(ArrayList<ImageKey> data,int dimention){
	            double[] d =new double[data.size()];
	            int i=0;
	            for(ImageKey image:data){
	            	double k[]=image.getImageKey();
	                d[i++]=k[dimention];
	            }
	            return findPos(d, 0, d.length-1, d.length/2);
	        }
	        
	        static double[][] maxmin(ArrayList<ImageKey> data,int dimentions){
	            double[][] mm = new double[2][dimentions];
	            //初始化 第一行为min，第二行为max
	            for(int i=0;i<dimentions;i++){
	                mm[0][i]=mm[1][i]=data.get(0).getImageKey()[i];
	                for(int j=1;j<data.size();j++){
	                    double[] d = data.get(j).getImageKey();
	                    if(d[i]<mm[0][i]){
	                        mm[0][i]=d[i];
	                    }else if(d[i]>mm[1][i]){
	                        mm[1][i]=d[i];
	                    }
	                }
	            }
	            return mm;
	        }
	        
	        static double distance(double[] a,double[] b){
	            double sum = 0;
	            for(int i=0;i<a.length;i++){
	                sum+=Math.pow(a[i]-b[i], 2);
	            }
	            return sum;
	        }
	        
	        /**
	         * 在max和min表示的超矩形中的点和点a的最小距离
	         * @param a 点a
	         * @param max 超矩形各个维度的最大值
	         * @param min 超矩形各个维度的最小值
	         * @return 超矩形中的点和点a的最小距离
	         */
	        static double mindistance(double[] a,double[] max,double[] min){
	            double sum = 0;
	            for(int i=0;i<a.length;i++){
	                if(a[i]>max[i])
	                    sum += Math.pow(a[i]-max[i], 2);
	                else if (a[i]<min[i]) {
	                    sum += Math.pow(min[i]-a[i], 2);
	                }
	            }
	            
	            return sum;
	        }
	        
	        /**
	         * 使用快速排序，查找排序后位置在point处的值
	         * 比Array.sort()后去对应位置值，大约快30%
	         * @param data 数据
	         * @param low 参加排序的最低点
	         * @param high 参加排序的最高点
	         * @param point 位置
	         * @return
	         */
	        private static double findPos(double[] data,int low,int high,int point){
	            int lowt=low;
	            int hight=high;
	            
	            double v = data[low];
	           
	            ArrayList<Integer> same = new ArrayList<Integer>((int)((high-low)*0.25));
	            while(low<high){
	                while(low<high&&data[high]>=v){
	                    if(data[high]==v){
	                        same.add(high);
	                    }
	                    high--;
	                }
	                data[low]=data[high];
	                while(low<high&&data[low]<v)
	                    low++;
	                data[high]=data[low];
	            }
	            data[low]=v;
	            int upper = low+same.size();
	            if (low<=point&&upper>=point) {
	                return v;
	            }
	            
	            if(low>point){
	                return findPos(data, lowt, low-1, point);
	            }
	            
	            int i=low+1;
	            for(int j:same){
	                if(j<=low+same.size())
	                    continue;
	                while(data[i]==v)
	                    i++;
	                data[j]=data[i];
	                data[i]=v;
	                i++;
	            }
	            
	            return findPos(data, low+same.size()+1, hight, point);
	        }
	    }
	    private KDTree() {}
	    /**
	     * 构建树
	     * @param input 输入
	     * @return KDTree树
	     */
	    public static KDTree build(ArrayList<ImageKey> input){
	        int m = input.get(0).getImageKey().length;
	        
	       
	        KDTree tree = new KDTree();
	        tree.kdtree = tree.new Node();
	        tree.buildDetail(tree.kdtree, input, m);
	        
	        return tree;
	    }
	    /**
	     * 循环构建树
	     * @param node 节点
	     * @param data 数据
	     * @param dimentions 数据的维度
	     */
	    private void buildDetail(Node node,ArrayList<ImageKey> data,int dimentions){
	        if(data.size()==1){
	            node.isLeaf=true;
	            node.value=data.get(0).getImageKey();
	            node.path.add(data.get(0).getPath());
	            return;
	        }
	        
	        //选择方差最大的维度
	        node.partitionDimention=-1;
	        double var = -1;
	        double tmpvar;
	        for(int i=0;i<dimentions;i++){
	            tmpvar=UtilZ.variance(data, i);
	            if (tmpvar>var){
	                var = tmpvar;
	                node.partitionDimention = i;
	            }
	        }
	        //如果方差=0，表示所有数据都相同，判定为叶子节点
	        if(var==0){
	            node.isLeaf=true;
	            node.value=data.get(0).getImageKey();
	            for(ImageKey image:data){
	            	node.path.add(image.getPath());
	            }
	            return;
	        }
	        
	        //选择分割的值
	        node.partitionValue=UtilZ.median(data, node.partitionDimention);
	        
	        double[][] maxmin=UtilZ.maxmin(data, dimentions);
	        node.min = maxmin[0];
	        node.max = maxmin[1];
	        
	        int size = (int)(data.size()*0.55);
	        ArrayList<ImageKey> left = new ArrayList<ImageKey>(size);
	        ArrayList<ImageKey> right = new ArrayList<ImageKey>(size);
	        
	        for(ImageKey image:data){
	        	double[] d=image.getImageKey();
	            if (d[node.partitionDimention]<node.partitionValue) {
	                left.add(image);
	            }else {
	                right.add(image);
	            }
	        }
	        Node leftnode = new Node();
	        Node rightnode = new Node();
	        node.left=leftnode;
	        node.right=rightnode;
	        if(left.size()==0||right.size()==0){
	        	 node.isLeaf=true;
		            node.value=data.get(0).getImageKey();
		            for(ImageKey image:data){
		            	node.path.add(image.getPath());
		            }
		            return;
	        }
	        buildDetail(leftnode, left, dimentions);
	        buildDetail(rightnode, right, dimentions);
	    }
	    /**
	     * 打印树，测试时用
	     */
	    public void print(){
	        printRec(kdtree,0);
	    }
	    private void printRec(Node node,int lv){
	        if(!node.isLeaf){
	            for(int i=0;i<lv;i++)
	                System.out.print("--");
	            System.out.println(node.partitionDimention+":"+node.partitionValue);
	            printRec(node.left,lv+1);
	            printRec(node.right,lv+1);
	        }else {
	            for(int i=0;i<lv;i++)
	                System.out.print("--");
	            StringBuilder s = new StringBuilder();
	            s.append('(');
	            for(int i=0;i<node.value.length-1;i++){
	                s.append(node.value[i]).append(',');
	            }
	            s.append(node.value[node.value.length-1]).append(')');
	            System.out.println(s);
	        }
	    }
	    public ArrayList<String> query(double[] input){
	        Node node = kdtree;
	        Stack<Node> stack = new Stack<Node>();
	        while(!node.isLeaf){
	            if(input[node.partitionDimention]<node.partitionValue){
	                stack.add(node.right);
	                node=node.left;
	            }else{
	                stack.push(node.left);
	                node=node.right;
	            }
	        }
	        /**
	         * 首先按树一路下来，得到一个想对较近的距离，再找比这个距离更近的点
	         */
	        double distance = UtilZ.distance(input, node.value);
	        ArrayList<String> nearest=queryRec(input, distance, stack);
	      if(nearest==null){
	    	 return node.path; 
	      }else{
	    	  return nearest;
	      }
	    }
	    public ArrayList<String> queryRec(double[] input,double distance,Stack<Node> stack){
	    	 ArrayList<String> nearest = null;
	        Node node = null;
	        double tdis;
	        while(stack.size()!=0){
	            node = stack.pop();
	            if(node.isLeaf){
	                 tdis=UtilZ.distance(input, node.value);
	                 if(tdis<distance){
	                     distance = tdis;
	                     nearest = node.path;
	                 }
	            }else {
	                /*
	                 * 得到该节点代表的超矩形中点到查找点的最小距离mindistance
	                 * 如果mindistance<distance表示有可能在这个节点的子节点上找到更近的点
	                 * 否则不可能找到
	                 */
	                double mindistance = UtilZ.mindistance(input, node.max, node.min);
	                if (mindistance<distance) {
	                    while(!node.isLeaf){
	                        if(input[node.partitionDimention]<node.partitionValue){
	                            stack.add(node.right);
	                            node=node.left;
	                        }else{
	                            stack.push(node.left);
	                            node=node.right;
	                        }
	                    }
	                    tdis=UtilZ.distance(input, node.value);
	                    if(tdis<distance){
	                        distance = tdis;
	                        nearest = node.path;
	                    }
	                }
	            }
	        }
	        return nearest;
	    }
	    public ArrayList<String> queryP(double[] input,int k){
	    	MaxHeap<ImageForSort> maxHeap=new MaxHeap<ImageForSort>(k);
	        Node node = kdtree;
	        Comparator<NodeP> OrderNP = new Comparator<NodeP>() {
	            public int compare(NodeP o1, NodeP o2) {
	                double numbera = o1.p;
	                double numberb = o2.p;
	                if (numberb > numbera) {
	                    return 1;
	                }

	                else if (numberb < numbera) {
	                    return -1;
	                } else {
	                    return 0;
	                }
	            }

	        };
	        Queue<NodeP> priorityQueue=new PriorityQueue<NodeP>(OrderNP);

	        
	        
	        while(!node.isLeaf){
	        	double d=Math.abs(input[node.partitionDimention]-node.partitionValue);
	            if(input[node.partitionDimention]<node.partitionValue){
	            	NodeP np=new NodeP(node.right,d);
	            	priorityQueue.add(np);
	                node=node.left;
	            }else{
	            	NodeP np=new NodeP(node.left,d);
	            	priorityQueue.add(np);
	                node=node.right;
	            }
	        }
	        /**
	         * 首先按树一路下来，得到一个想对较近的距离，再找比这个距离更近的点
	         */
	        double distance = UtilZ.distance(input, node.value);
	      
	        for(int i=0;i<node.path.size();i++){
	        	ImageForSort imF=new ImageForSort();
	        	imF.setDistance(distance);
	        	imF.setPath(node.path.get(i));
	        	maxHeap.insert(imF);
	        }
	        queryRecP(input, distance, priorityQueue,maxHeap,k);
	        ArrayList<String> result=new ArrayList<String>(maxHeap.getCurrentSize());
	        while(!maxHeap.isEmpty()){
	        	result.add( maxHeap.deleMax().getPath());
	        	
	        }
	     return result;
	    }
	    public void queryRecP(double[] input,double distance,  Queue<NodeP> priorityQueue,MaxHeap<ImageForSort> maxHeap,int k){
	        Node node = null;
	        double tdis;
	        while(priorityQueue.size()!=0){
	            node = priorityQueue.poll().node;
	            if(node.isLeaf){
	                 tdis=UtilZ.distance(input, node.value);
	                 if(tdis<maxHeap.getMax().getDistance()||maxHeap.getCurrentSize()<k){
	                 for(int i=0;i<node.path.size();i++){
	         	        	ImageForSort imF=new ImageForSort();
	         	        	imF.setDistance(tdis);
	         	        	imF.setPath(node.path.get(i));
	         	        	maxHeap.insert(imF);
	         	        }
	                 distance=maxHeap.getMax().getDistance();
	                 }
	                
	            }else {
	                /*
	                 * 得到该节点代表的超矩形中点到查找点的最小距离mindistance
	                 * 如果mindistance<distance表示有可能在这个节点的子节点上找到更近的点
	                 * 否则不可能找到
	                 */
	                double mindistance = UtilZ.mindistance(input, node.max, node.min);
	                if (mindistance<distance||maxHeap.getCurrentSize()<k) {
//	                if(true){
	                	double d=Math.abs(input[node.partitionDimention]-node.partitionValue);
	                    while(!node.isLeaf){
	                        if(input[node.partitionDimention]<node.partitionValue){
	                        	NodeP np=new NodeP(node.right,d);
	                        	priorityQueue.add(np);
	                            node=node.left;
	                        }else{
	                         	NodeP np=new NodeP(node.left,d);
	                        	priorityQueue.add(np);
	                            node=node.right;
	                        }
	                    }
	                    tdis=UtilZ.distance(input, node.value);
	                    if(tdis<distance||maxHeap.getCurrentSize()<k){
	                    	 for(int i=0;i<node.path.size();i++){
	 	         	        	ImageForSort imF=new ImageForSort();
	 	         	        	imF.setDistance(tdis);
	 	         	        	imF.setPath(node.path.get(i));
	 	         	        	maxHeap.insert(imF);
	 	         	        }
	 	                 distance=maxHeap.getMax().getDistance();
	                    }
	                }
	            }
	        }
	      
	    }
	public static void main(String[] args) {
		ArrayList<ImageKey>  ikList=new ArrayList<ImageKey>();
		for(int i=0;i<20;i++){
			ImageKey ik=new ImageKey();
			ik.setPath(""+i);
			double[] m={i,i,i};
			ik.setImageKey(m);
			ikList.add(ik);
		}
		KDTree	kt=KDTree.build(ikList);
		double []a={11,11};
		System.out.println(kt.queryP(a, 5));
	}

}
