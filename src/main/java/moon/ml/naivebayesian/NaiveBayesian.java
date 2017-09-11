package moon.ml.naivebayesian;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moon.ml.record.RecordWithFeaturesString;

/**
 * @ClassName NaiveBayesian
 * @Description 朴素贝叶斯分类器
 *              对于给出的待分类特征向量，求出此特征时出现各个类别的概率，
 *              最大概率的类别就认为是哪一类别
 * @author "liumingxin"
 * @Date 2017年6月23日 上午11:07:16
 * @version 1.0.0
 */
public class NaiveBayesian {
	 
	/**
	 * 读取测试文档
	 */
	public static List<RecordWithFeaturesString> readTest(String fileIn) {
		List<RecordWithFeaturesString> outList = new ArrayList<RecordWithFeaturesString>();
		try {
			File file = new File(fileIn);
			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			String line = null;
			while ((line = in.readLine()) != null) {
				RecordWithFeaturesString record = new RecordWithFeaturesString();
				List<String> list = new ArrayList<String>();
				String[] mArray = line.split(",");
				for (Integer i = 0; i < mArray.length - 1; i++) {
					list.add(mArray[i]);
				}
				record.setFeatures(list);
				record.setCategory(mArray[mArray.length - 1]);
				outList.add(record);
			}
			in.close();
			reader.close();
		} catch (Exception e) {
			System.out.println("读取出错");
			e.printStackTrace();
		}
		return outList;
	}

	/**
	 * @Title: getCategoryRecordsMapper
	 * @Description: 得到类型-记录映射
	 * @param trainList
	 * @return
	 * @return Map<String,List<RecordWithFeaturesString>>
	 */
	private static Map<Object, List<RecordWithFeaturesString>> getCategoryRecordsMapper(
			List<RecordWithFeaturesString> trainList) {
		Map<Object, List<RecordWithFeaturesString>> categoryRecordsMapper = new HashMap<Object, List<RecordWithFeaturesString>>();
		for (RecordWithFeaturesString record : trainList) {
			Object category = record.getCategory();
			List<RecordWithFeaturesString> list = categoryRecordsMapper.get(category);
			if (list == null) {
				list = new ArrayList<RecordWithFeaturesString>();
			}
			list.add(record);
			categoryRecordsMapper.put(category, list);
		}
		return categoryRecordsMapper;
	}

	/**
	 * @Title: calFeatureIProbability
	 * @Description: 计算第I个特征在集合中的概率，用于计算P(特征1|结果1)
	 * @param records
	 * @param testList
	 * @param i
	 * @return
	 * @return double
	 */
	private static double calFeatureIProbability(List<RecordWithFeaturesString> records, List<String> testList, int i) {
		int fiCount = 0;
		int totalSize = records.size();
		// 在类别C出现时第I个特征出现的概率
		double featureIProbability = 1d;
		for (RecordWithFeaturesString record : records) {
			List<String> features = record.getFeatures();
			if (features.size() == testList.size()) {
				String test = testList.get(i);
				String feature = features.get(i);
				if (test.equals(feature)) {
					fiCount++;
				}
			}
		}
		
		//P(特征1，特征2，特征3|结果1) = P(特征1|结果1)*P(特征2|结果1)*P(特征3|结果1)
		//当单一特征出现的概率=0，那么整个结果的概率就是0，这样就会造成单一特征，影响整体结果（这个影响太大了），
		//为了避免单一特征主导影响整体情况,当频次等于0的时候，把分子分母都加1，表示此特征出现的概率特别特别的小
		if(fiCount == 0){
			fiCount++;
			totalSize++;
		}
		featureIProbability = (double) fiCount / totalSize;
		return featureIProbability;
	}

	/**
	 * @Title: getResult
	 * @Description: 得到结果
	 * @param trainList
	 *            训练集合
	 * @param testList
	 *            测试数据
	 * @return Object
	 */
	public static Object getResult(List<RecordWithFeaturesString> trainList, List<String> testList) {
		int totalTrainList = trainList.size();

		// 类型-记录映射
		Map<Object, List<RecordWithFeaturesString>> categoryRecordsMapper = getCategoryRecordsMapper(trainList);
		// 类型-概率映射，也就是最终结果
		Map<Object, Double> categoryProbabilityMapper = new HashMap<Object, Double>();

		// 计算类别概率
		for (Object category : categoryRecordsMapper.keySet()) {
			List<RecordWithFeaturesString> records = categoryRecordsMapper.get(category);
			// 类别概率
			double categoryProbability = (double) records.size() / totalTrainList;
			// 在类别C出现时第I个特征出现的概率
			double featureIProbability = 1d;
			// P(特征1，特征2，特征3|结果1) = P(特征1|结果1)*P(特征2|结果1)*P(特征3|结果1)，假设特征123相互独立
			for (int i = 0; i < testList.size(); i++) {
				featureIProbability = featureIProbability * calFeatureIProbability(records, testList, i);
			}
			// P(结果1|特征1，特征2，特征3) =
			// P(特征1，特征2，特征3|结果1)*P(结果1),因为P(特征1，特征2，特征3)相等，所以省略
			featureIProbability = featureIProbability * categoryProbability;
			categoryProbabilityMapper.put(category, featureIProbability);
		}

		Object maxCategory = null;
		double maxProbability = 0d;
		for (Object category : categoryProbabilityMapper.keySet()) {
			double probability = categoryProbabilityMapper.get(category);
			if (probability > maxProbability) {
				maxProbability = probability;
				maxCategory = category;
			}
		}

		return maxCategory;
	}

	public static void main(String[] args) {

		//给定十个特征，求出数据哪一类别
		String test1 = "6,1,9,1,2,3,1,2,2,1";
		String[] testArr = test1.split(",");
		List<String> testList = new ArrayList<String>();
		for (String str : testArr) {
			testList.add(str);
		}

		String trainTxt = "data/naivebayesian/naivebayesian1.txt";
		// 读取训练数据
		List<RecordWithFeaturesString> trainList = readTest(trainTxt);
		Object result = getResult(trainList, testList);
		System.out.println(result);
	}

}
