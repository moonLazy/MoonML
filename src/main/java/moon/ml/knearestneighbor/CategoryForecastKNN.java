package moon.ml.knearestneighbor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moon.ml.record.DistanceMapperDouble;
import moon.ml.record.RecordWithFeaturesDouble;

/**
 * @ClassName CategoryForecastKNN
 * @Description 类别预测KNN方法类
 * @author "liumingxin"
 * @Date 2017年6月20日 下午3:56:18
 * @version 1.0.0
 */
public class CategoryForecastKNN extends KNearestNeighbor{

	@Override
	public Object getCategory(List<DistanceMapperDouble> mappers) {

		Map<Object,ResultInfo> resultInfoMap = new HashMap<Object,ResultInfo>();
		for(DistanceMapperDouble mapper : mappers){
			RecordWithFeaturesDouble data = mapper.getData();
			Object classify = data.getCategory();
			//种类为KEY
			ResultInfo resultInfo = resultInfoMap.get(classify);
			if(resultInfo == null){
				resultInfo = new ResultInfo();
			}
			resultInfo.setClassify(classify);
			//频次+1
			resultInfo.setFrequency(resultInfo.getFrequency()+1);
			//总距离相加
			resultInfo.setTotalDistance(resultInfo.getTotalDistance()+mapper.getDistance());
			resultInfoMap.put(classify, resultInfo);
		}

		
		List<ResultInfo> resultInfos = new ArrayList<ResultInfo>();
		for(Object key :resultInfoMap.keySet()){
			resultInfos.add(resultInfoMap.get(key));
		}
		Collections.sort(resultInfos);
		return resultInfos.get(0).getClassify();
	
	}

	public static void main(String[] args) {

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
		
		List<RecordWithFeaturesDouble> trainList = new ArrayList<RecordWithFeaturesDouble>();
		for(List<String> l : trains){
			List<Double> ds = new ArrayList<Double>();
			for(int i=0;i<l.size()-1;i++){
				ds.add(Double.parseDouble(l.get(i)));
			}
			RecordWithFeaturesDouble r = new RecordWithFeaturesDouble();
			r.setCategory(l.get(l.size()-1));
			r.setFeatures(ds);
			trainList.add(r);
		}
		
		List<Double> testList = new ArrayList<Double>();
		testList.add(4d);
		testList.add(488d);
		testList.add(7564d);
		testList.add(0.66d);
		CategoryForecastKNN knn = new CategoryForecastKNN();
		String result = knn.getResult(trainList, testList, 3).toString();
		System.out.println(result);
	}

}
