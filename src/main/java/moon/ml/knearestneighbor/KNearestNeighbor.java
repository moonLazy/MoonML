package moon.ml.knearestneighbor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moon.ml.record.DistanceMapperDouble;
import moon.ml.record.RecordWithFeaturesDouble;
import moon.ml.util.Uniformization;

/**
 * @ClassName KNearestNeighbor
 * @Description 分类预测
 * 				适用于数值型特征值的预测
 * @author "liumingxin"
 * @Date 2017年5月23日 上午9:55:10
 * @version 1.0.0
 */
public abstract class KNearestNeighbor {

	/**
	 * @Title: normalization
	 * @Description: 数据集的归一化处理
	 * @param dataSet
	 * @return
	 * @return List<RecordWithFeaturesDouble>
	 */
	public List<RecordWithFeaturesDouble> normalization(List<RecordWithFeaturesDouble> dataSet){
		List<RecordWithFeaturesDouble> result = new ArrayList<RecordWithFeaturesDouble>();
		List<List<Double>> doubleList = new ArrayList<List<Double>>();
		for(RecordWithFeaturesDouble r : dataSet){
			doubleList.add(r.getFeatures());
		}
		List<List<Double>> normalResults = Uniformization.getUniformizationResult(doubleList);
		for(int i=0;i<normalResults.size();i++){
			RecordWithFeaturesDouble r = new RecordWithFeaturesDouble();
			r.setCategory(dataSet.get(i).getCategory());
			r.setFeatures(normalResults.get(i));
			result.add(r);
		}
		return result;
	}
	
	/**
	 * @Title: getResult
	 * @Description: 得出结果
	 * @param dataSet 数据集
	 * @param test 测试数据
	 * @param K K值
	 * @return
	 * @return Object
	 */
	public Object getResult(List<RecordWithFeaturesDouble> dataSet, List<Double> test, Integer K){
		//将测试数据，加入到dataset中，然后归一化处理，再分离测试数据
		RecordWithFeaturesDouble testRecord = new RecordWithFeaturesDouble();
		testRecord.setFeatures(test);
		dataSet.add(testRecord);
		
		dataSet = normalization(dataSet);
		testRecord = dataSet.get(dataSet.size()-1);
		dataSet = dataSet.subList(0, dataSet.size()-1);
		
		List<DistanceMapperDouble> mappers = new ArrayList<DistanceMapperDouble>();
		for(RecordWithFeaturesDouble data : dataSet){
			//计算两个向量的距离
			Double distance = data.featuresList2RealVector().getDistance(testRecord.featuresList2RealVector());
			DistanceMapperDouble mapper = new DistanceMapperDouble();
			mapper.setData(data);
			mapper.setDistance(distance);
			mappers.add(mapper);
		}
		//根据聚类升序排序
		Collections.sort(mappers);
		//截取前K个，这就是所有的K邻近中的K的作用
		mappers = mappers.subList(0, K);
		return getCategory(mappers);
	}
	
	/**
	 * @Title: getCategory
	 * @Description: 自定义策略
	 * @param mappers
	 * @return
	 * @return Object
	 */
	public abstract Object getCategory(List<DistanceMapperDouble> mappers);
	
}
