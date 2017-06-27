package moon.ml.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moon.ml.record.RecordWithFeaturesDouble;

/**
 * @ClassName Uniformization
 * @Description 归一化计算类
 * @author "liumingxin"
 * @Date 2017年5月22日 下午1:47:30
 * @version 1.0.0
 */
public class Uniformization {


	/**
	 * @Title: getUniformizationResult
	 * @Description: 归一化结果
	 * @param list
	 * @return
	 * @return List<List<Double>>
	 */
	public static List<List<Double>> getUniformizationResult(List<List<Double>> list){
		if(list == null){
			return null;
		}
		List<List<Double>> results = new ArrayList<List<Double>>();
		for(int i=0;i<list.size();i++){
			results.add(new ArrayList<Double>());
		}
		int colSize = list.get(0).size();
		int rowSize = list.size();
		for(int i=0;i<colSize;i++){
			Double maxElement = list.get(0).get(i);
			Double minElement = list.get(0).get(i);
			//获取每一列的最大值最小值
			for(List<Double> strs : list){
				Double element = strs.get(i);
				if(element > maxElement){
					maxElement = element;
				}
				if(element < minElement){
					minElement = element;
				}
			}
			
			//归一化
			for(int j=0;j<rowSize;j++){
				List<Double> result = results.get(j); 
				if(result == null){
					result = new ArrayList<Double>();
					results.add(result);
				}
				Double element = list.get(j).get(i);
				element = (element - minElement)/(maxElement - minElement);
				result.add(element);
				
			}
		}
		return results;
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
		
		List<List<Double>> l = new ArrayList<List<Double>>();
		for(RecordWithFeaturesDouble r : trainList){
			l.add(r.getFeatures());
		}
		List<List<Double>> results = getUniformizationResult(l);
		for(List<Double> result : results){
			System.out.println(result);
		}
	}
}
