package moon.ml.logisticRegression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * @ClassName LogisticRegression
 * @Description 逻辑回归，数值型二分类算法
 * @author "liumingxin"
 * @Date 2017年9月11日 下午1:23:31
 * @version 1.0.0
 */
public class LogisticRegression {
	
	/**
	 * @Fields alpha : 步长
	 */
	private double alpha;
	/**
	 * @Fields maxCycles : 最大循环次数
	 */
	private int maxCycles;
	
	public LogisticRegression(){
		this.alpha = 0.001;
		this.maxCycles = 500;
	}
	
	public LogisticRegression(double alpha, int maxCycles){
		this.alpha = alpha;
		this.maxCycles = maxCycles;
	}
	
	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public int getMaxCycles() {
		return maxCycles;
	}

	public void setMaxCycles(int maxCycles) {
		this.maxCycles = maxCycles;
	}

	/**
	 * 读取测试文档，转换为矩阵
	 */
	public Map<String,RealMatrix> readDataSet(String fileIn){
		Map<String,RealMatrix> map = new HashMap<String,RealMatrix>();
		List<List<Double>> dataList = new ArrayList<List<Double>>();
		List<Double> labelList = new ArrayList<Double>();
		try {
			File file = new File(fileIn);
			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			String line = null;
			while ((line = in.readLine()) != null) {
				String[] mArray = line.split("\t");
				List<Double> data = new ArrayList<Double>();
				data.add(1.0);
				for(int i=0;i<mArray.length;i++){
					if(i != mArray.length-1){
						data.add(Double.parseDouble(mArray[i]));
					}else{
						labelList.add(Double.parseDouble(mArray[i]));
					}
				}
				dataList.add(data);
			}
			
			//转换为矩阵
			int m = dataList.size();
			int n = dataList.get(0).size();
			double[][] dataArray = new double[m][n];
			for(int i=0;i<dataList.size();i++){
				List<Double> datas = dataList.get(i);
				for(int j=0;j<datas.size();j++){
					dataArray[i][j] = datas.get(j);
				}
			}
			
			RealMatrix dataMat = new Array2DRowRealMatrix(dataArray);
			int k = labelList.size();
			double[] labelArray = new double[k];
			for(int i=0;i<k;i++){
				labelArray[i] = labelList.get(i);
			}
			RealMatrix labelMat = (new Array2DRowRealMatrix(labelArray));
			
			map.put("data", dataMat);
			map.put("label", labelMat);
			
			in.close();
			reader.close();
			
		} catch (Exception e) {
			System.out.println("读取出错");
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * @Title: sigmoid
	 * @Description: sigmoid函数矩阵计算
	 * @param matrix
	 * @return
	 * @return RealMatrix
	 */
	public RealMatrix sigmoid(RealMatrix matrix){
		double[][] arr = matrix.getData();
		int row = matrix.getRowDimension();
		int col = matrix.getColumnDimension();
		double[][] resultArr = new double[row][col];
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				resultArr[i][j] = sigmoid(arr[i][j]);
			}
		}
		return new Array2DRowRealMatrix(resultArr);
	}
	
	/**
	 * @Title: sigmoid
	 * @Description: 1/(1+exp(-d))
	 * @param data
	 * @return
	 * @return double
	 */
	public double sigmoid(double d){
		return 1 / (1+Math.pow(Math.E, -d));
	}
	
	/**
	 * @Title: train
	 * @Description: 训练
	 * @param dataSetFilePath
	 * @return
	 * @return RealMatrix
	 */
	public RealMatrix train(String dataSetFilePath){
		//读取训啦数据集
		Map<String,RealMatrix> map = readDataSet(dataSetFilePath);
		//特征矩阵
		RealMatrix data = map.get("data");
		
		//类别矩阵
		RealMatrix label = map.get("label");
		
		//数据的列
		int column = data.getColumnDimension();
		double[][] initWeightsArray = new double[column][1];
		for(int i=0;i<column;i++){
			initWeightsArray[i][0] = 1;
		}
		//待定系数向量
		RealMatrix weight = new Array2DRowRealMatrix(initWeightsArray);
		for(int i=0;i<this.maxCycles;i++){
			//数据矩阵 * 系数向量
			RealMatrix a = data.multiply(weight);
			//预测函数 
			RealMatrix h = sigmoid(a);
			//误差 实际-理论
			RealMatrix error = label.subtract(h);
			//梯度上升法 w = data转置*步长*误差 +w 
			weight = weight.add(data.transpose().scalarMultiply(this.alpha).multiply(error));
		}
		return weight;
		
	}
	
	public static void main(String[] args) {
		
		
		String dataSetFilePath = "data/logisticRegression/testSet.txt";
		LogisticRegression log = new LogisticRegression(0.001, 500);
		//系数矩阵(计算时三者用一个就好，第一个系数作为常数项，所以在进行预测时，应该在第一个特征值前加一个值为1的特征)
		RealMatrix coefficientMatrix = log.train(dataSetFilePath);	
		//把系数矩阵转换成向量
		RealVector coefficientVector = coefficientMatrix.getColumnVector(0);
		//系数数组
		double[] coefficientArray = coefficientVector.toArray();
		
		//第一个元素为常数项
		double[] test = new double[]{1,-0.017612,14.053064};
		//得分
		double score = 0;
		for(int i=0;i<coefficientArray.length;i++){
			score += coefficientArray[i] * test[i];
		}
		//带入sigmoid函数
		int result = log.sigmoid(score) >= 0.5 ? 1 : 0;
		
		System.out.println(result);
		
	}

}
