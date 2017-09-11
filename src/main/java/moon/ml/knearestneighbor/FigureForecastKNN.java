package moon.ml.knearestneighbor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import moon.ml.record.DistanceMapperDouble;
import moon.ml.record.RecordWithFeaturesDouble;

/**
 * @ClassName FigureForecastKNN
 * @Description 数字预测KNN方法类
 * @author "liumingxin"
 * @Date 2017年6月20日 下午3:56:39
 * @version 1.0.0
 */
public class FigureForecastKNN extends KNearestNeighbor{

	/* (非 Javadoc)
	 * <p>Title: getCategory</p>
	 * <p>Description: 求K个结果的平均值，作为预测结果</p>
	 * @param mappers
	 * @return
	 * @see moon.ml.knearestneighbor.KNearestNeighbor#getCategory(java.util.List)
	 */
	@Override
	public Object getCategory(List<DistanceMapperDouble> mappers) {

		Double result = 0d;
		for (DistanceMapperDouble mapper : mappers) {
			result += Double.parseDouble(mapper.getData().getCategory().toString());
		}
		result = result / mappers.size();
		return result;

	}

	
	/**
	 * 读取测试文档
	 */
	public static List<RecordWithFeaturesDouble> readTest(String fileIn) {
		List<RecordWithFeaturesDouble> outList = new ArrayList<RecordWithFeaturesDouble>();
		try {
			File file = new File(fileIn);
			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			String line = null;
			while ((line = in.readLine()) != null) {
				RecordWithFeaturesDouble record = new RecordWithFeaturesDouble();
				List<Double> list = new ArrayList<Double>();
				String[] mArray = line.split(",");
				for (Integer i = 0; i < mArray.length - 1; i++) {
					list.add(Double.parseDouble(mArray[i]));
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
	
	
	public static void main(String[] args) {

		
		//K值
		Integer K = 10;
		
		List<RecordWithFeaturesDouble> trainList = readTest("data/knn/knn.txt");
		List<Double> testList = new ArrayList<Double>();
		testList.add(6.14);
		testList.add(6.28);
		testList.add(6.42);
		testList.add(6.38);
		FigureForecastKNN knn = new FigureForecastKNN();
		Object theoretical =  knn.getResult(trainList, testList, K);
		System.out.println(theoretical);
	}

}
