package moon.ml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import moon.ml.decisiontree.Id3;
import moon.ml.decisiontree.TreeNode;
import moon.ml.kmean.KMean;
import moon.ml.kmean.KMeanResult;
import moon.ml.knearestneighbor.CategoryForecastKNN;
import moon.ml.knearestneighbor.FigureForecastKNN;
import moon.ml.logisticRegression.LogisticRegression;
import moon.ml.naivebayesian.NaiveBayesian;
import moon.ml.record.RecordWithFeaturesDouble;
import moon.ml.record.RecordWithFeaturesString;

public class App {

	public static void main(String[] args) {
		 iD3Demo();
		 categoryKNNDemo();
		 figureKNNDemo();
		 nbDemo();
		 logisticRegresstionDemo();
		 kmeanDemo();
	}

	/**
	 * @Title: ID3Demo
	 * @Description: 决策树ID3DEMO
	 * @return void
	 */
	public static void iD3Demo() {
		// 训练数据
		List<List<String>> trains = new ArrayList<List<String>>();
		trains.add(Arrays.asList("sunny", "hot", "high", "FALSE", "no"));
		trains.add(Arrays.asList("sunny", "hot", "high", "TRUE", "no"));
		trains.add(Arrays.asList("overcast", "hot", "high", "FALSE", "yes"));
		trains.add(Arrays.asList("rainy", "mild", "high", "FALSE", "yes"));
		trains.add(Arrays.asList("rainy", "cool", "normal", "FALSE", "yes"));
		trains.add(Arrays.asList("rainy", "cool", "normal", "TRUE", "no"));
		trains.add(Arrays.asList("overcast", "cool", "normal", "TRUE", "yes"));
		trains.add(Arrays.asList("sunny", "mild", "high", "FALSE", "no"));
		trains.add(Arrays.asList("sunny", "cool", "normal", "FALSE", "yes"));
		trains.add(Arrays.asList("rainy", "mild", "normal", "FALSE", "yes"));
		trains.add(Arrays.asList("sunny", "mild", "normal", "TRUE", "yes"));
		trains.add(Arrays.asList("overcast", "mild", "high", "TRUE", "yes"));
		trains.add(Arrays.asList("overcast", "hot", "normal", "FALSE", "yes"));
		trains.add(Arrays.asList("rainy", "mild", "high", "TRUE", "no"));

		// 特征名称和结果名称，最后一个是结果的名称
		List<String> columnName = Arrays.asList("A", "B", "C", "D", "E");

		Id3 id = new Id3();
		id.setFeatureNames(columnName);
		// 创建树，可以此决策树的结构保存，便于以后使用，不需要每一次分类都重新生成一颗树
		TreeNode root = id.createTree(trains);

		// 测试数据
		Map<String, String> mapTest = new HashMap<String, String>();
		mapTest.put("A", "sunny");
		mapTest.put("B", "mild");
		mapTest.put("C", "high");
		mapTest.put("D", "FALSE");

		// 得出结果
		String result = id.getResult(root, mapTest);
		System.out.println(result);
	}

	/**
	 * @Title: CategoryKNNDemo
	 * @Description: KNN类别预测
	 * @return void
	 */
	public static void categoryKNNDemo() {
		// 样本数据
		List<List<String>> trains = new ArrayList<List<String>>();
		trains.add(Arrays.asList("2", "357", "9888", "0.54", "no"));
		trains.add(Arrays.asList("3", "452", "8888", "0.35", "no"));
		trains.add(Arrays.asList("1", "335", "7894", "0.13", "yes"));
		trains.add(Arrays.asList("7", "645", "5648", "0.97", "yes"));
		trains.add(Arrays.asList("6", "255", "6794", "0.65", "yes"));
		trains.add(Arrays.asList("8", "388", "8015", "0.34", "no"));
		trains.add(Arrays.asList("4", "458", "6798", "0.66", "yes"));
		trains.add(Arrays.asList("6", "687", "8764", "0.79", "no"));
		trains.add(Arrays.asList("8", "388", "8754", "0.68", "yes"));
		trains.add(Arrays.asList("1", "546", "9768", "0.67", "yes"));
		trains.add(Arrays.asList("8", "488", "9537", "0.35", "yes"));
		trains.add(Arrays.asList("3", "612", "5491", "0.97", "yes"));
		trains.add(Arrays.asList("8", "548", "6724", "0.64", "yes"));
		trains.add(Arrays.asList("4", "356", "9768", "0.88", "no"));

		// 最终的训练样本数据集合需要封装成List<RecordWithFeaturesDouble>
		List<RecordWithFeaturesDouble> trainList = new ArrayList<RecordWithFeaturesDouble>();
		for (List<String> l : trains) {
			List<Double> ds = new ArrayList<Double>();
			for (int i = 0; i < l.size() - 1; i++) {
				ds.add(Double.parseDouble(l.get(i)));
			}
			RecordWithFeaturesDouble r = new RecordWithFeaturesDouble();
			r.setCategory(l.get(l.size() - 1));
			r.setFeatures(ds);
			trainList.add(r);
		}

		// 测试数据
		List<Double> testList = new ArrayList<Double>();
		testList.add(4d);
		testList.add(488d);
		testList.add(7564d);
		testList.add(0.66d);
		CategoryForecastKNN knn = new CategoryForecastKNN();
		// 得到结果
		String result = knn.getResult(trainList, testList, 3).toString();
		System.out.println(result);
	}

	/**
	 * @Title: figureKNNDemo
	 * @Description: 数值预测
	 * @return void
	 */
	public static void figureKNNDemo() {
		// K值
		Integer K = 10;
		FigureForecastKNN knn = new FigureForecastKNN();
		List<RecordWithFeaturesDouble> trainList = FigureForecastKNN.readTest("data/knn/knn.txt");
		List<Double> testList = new ArrayList<Double>();
		testList.add(6.14);
		testList.add(6.28);
		testList.add(6.42);
		testList.add(6.38);

		Object theoretical = knn.getResult(trainList, testList, K);
		System.out.println(theoretical);
	}

	/**
	 * @Title: nbDemo
	 * @Description: 朴素贝叶斯分类
	 * @return void
	 */
	public static void nbDemo() {
		// 给定十个特征，求出数据哪一类别
		String test1 = "6,1,9,1,2,3,1,2,2,1";
		String[] testArr = test1.split(",");
		List<String> testList = new ArrayList<String>();
		for (String str : testArr) {
			testList.add(str);
		}

		String trainTxt = "data/naivebayesian/naivebayesian1.txt";
		// 读取训练数据
		List<RecordWithFeaturesString> trainList = NaiveBayesian.readTest(trainTxt);
		Object result = NaiveBayesian.getResult(trainList, testList);
		System.out.println(result);
	}

	/**
	 * @Title: logisticRegresstionDemo
	 * @Description: 逻辑回归Demo
	 * @return void
	 */
	public static void logisticRegresstionDemo() {
		String dataSetFilePath = "data/logisticRegression/testSet.txt";
		// 参数分别是步长和最大迭代次数
		LogisticRegression log = new LogisticRegression(0.001, 500);
		// 系数矩阵(计算时三者用一个就好，第一个系数作为常数项，所以在进行预测时，应该在第一个特征值前加一个值为1的特征)
		RealMatrix coefficientMatrix = log.train(dataSetFilePath);
		// 把系数矩阵转换成向量
		RealVector coefficientVector = coefficientMatrix.getColumnVector(0);
		// 系数数组
		double[] coefficientArray = coefficientVector.toArray();

		// 第一个元素为常数项
		double[] test = new double[] { 1, -0.017612, 14.053064 };
		// 得分
		double score = 0;
		for (int i = 0; i < coefficientArray.length; i++) {
			score += coefficientArray[i] * test[i];
		}
		// 带入sigmoid函数
		int result = log.sigmoid(score) >= 0.5 ? 1 : 0;

		System.out.println(result);
	}

	/**
	 * @Title: kmeanDemo
	 * @Description: Kmean聚类Demo
	 * @return void
	 */
	public static void kmeanDemo() {
		KMean kmean = new KMean();
		String dataSetFilePath = "data/kmeans/testSet.txt";
		// 读取数据集
		RealMatrix data = kmean.readDataSet(dataSetFilePath);
		int maxIterations = 200;

		List<KMeanResult> result = kmean.train(data, 4, maxIterations);
		System.out.println(result);
	}
}
