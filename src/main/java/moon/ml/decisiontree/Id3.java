package moon.ml.decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import moon.ml.util.Log;


/**
 * @ClassName Id3
 * @Description ID3决策树
 * 				适用于标称型特征数据，禁止将类似ID，Date等，这样唯一值作为特征
 * 				
 * @author "liumingxin"
 * @Date 2017年6月20日 上午10:54:03
 * @version 1.0.0
 */
public class Id3 {
	
	private List<String> featureNames;
	
	public List<String> getFeatureNames() {
		return featureNames;
	}

	public void setFeatureNames(List<String> featureNames) {
		this.featureNames = featureNames;
	}
	
	/**
	 * @Title: createTree
	 * @Description: 创建树
	 * @param dataSet 数据集
	 * @param nameSet 数据集中列对应的名称
	 * @return
	 * @return TreeNode
	 */
	public TreeNode createTree(List<List<String>> dataSet){
		TreeNode root = new TreeNode();
		
		//根据当前数据集选择最好的划分方式
		SplitResult splitResult = chooseBestFeatureSplit(dataSet);
		
		//设置父节点
		root.setParent(null);
		//父节点属性
		root.setParentAttribute(null);
		
		//节点名称
		root.setName(this.featureNames.get(splitResult.getIndex()));
		root.setIndex(splitResult.getIndex());
		//节点属性集合
		root.setAttributes(splitResult.getAttributes());
			
			
		insertNode(dataSet, this.featureNames, root);
		return root;
	}
	
	
	
	/**
	 * @Title: insertNode
	 * @Description: 插入节点
	 * @param dataSet 数据集
	 * @param dataSetnames 数据集中列对应的名称
	 * @param parent 父节点
	 * @return void
	 */
	public void insertNode(List<List<String>> dataSet, List<String> dataSetnames, TreeNode parent){
		List<String> attributes = parent.getAttributes();
		
		for(String attribute : attributes){
			SplitDataSet splitDataSet = splitDataSet(dataSet, dataSetnames, parent.getIndex(), attribute);
			List<List<String>> list =  splitDataSet.getDataSet();
			List<String> nameSet = splitDataSet.getNameSet();
			//根据当前数据集选择最好的划分方式
			SplitResult splitResult = chooseBestFeatureSplit(list);
			//如果信息增益不等于0，继续划分
			if(splitResult.getInfoGain() != 0){
				//创建新节点
				TreeNode node = new TreeNode();
				//设置父节点
				node.setParent(parent);
				//父节点属性
				node.setParentAttribute(attribute);
				//节点名称
				node.setName(nameSet.get(splitResult.getIndex()));
				//节点对应当前数据集中的列序号
				node.setIndex(splitResult.getIndex());
				//节点属性集合
				node.setAttributes(splitResult.getAttributes());
				if(parent.getChildren() == null){
					parent.setChildren(new ArrayList<TreeNode>());
				}
				parent.getChildren().add(node);
				
				insertNode(list, nameSet, node);
			}else{
				//创建新节点
				TreeNode node = new TreeNode();
				//设置父节点
				node.setParent(parent);
				//父节点属性
				node.setParentAttribute(attribute);
				//节点名称
				node.setName(chooseBestResult(list));
				//节点对应数据集中的列序号
				node.setIndex(this.featureNames.size()-1);
				//节点属性集合
				node.setAttributes(null);
				if(parent.getChildren() == null){
					parent.setChildren(new ArrayList<TreeNode>());
				}
				parent.getChildren().add(node);
			}
		}
	}
	
	/**
	 * @Title: calCategoryEnt
	 * @Description: 计算类别的香农熵
	 * @param dataSet
	 * @return
	 * @return double
	 * 构建字典类，<类别，类别所出现的次数>
	 * 香农熵 = p(类别)*log2p(类别) 递减
	 */
	public double calCategoryEnt(List<List<String>> dataSet){
		Integer trainsCount = dataSet.size();
		//分类字典类
		Map<String,Integer> labelCount = new HashMap<String,Integer>();
		for(List<String> single : dataSet ){
			//每一条训练数据中的分类信息
			String currrentLabel = single.get(single.size()-1);
			//如果分类信息已经存在于分类字典中，数量+1，否则把该分类加入字典
			if(labelCount.containsKey(currrentLabel)){
				labelCount.put(currrentLabel, labelCount.get(currrentLabel)+1);
			}else{
				labelCount.put(currrentLabel, 1);
			}
		}
		double entropy = calEntropy(labelCount, trainsCount);
		return entropy;
	}
	
	/**
	 * @Title: chooseBestResult
	 * @Description: 叶子节点，选择最好的策略
	 * @param dataSet
	 * @return
	 * @return String
	 */
	public String chooseBestResult(List<List<String>> dataSet){
		String reuslt = "";
		Integer max = 0;
		//分类字典类
		Map<String,Integer> labelCount = new HashMap<String,Integer>();
		for(List<String> single : dataSet ){
			//每一条训练数据中的分类信息
			String currrentLabel = single.get(single.size()-1);
			//如果分类信息已经存在于分类字典中，数量+1，否则把该分类加入字典
			if(labelCount.containsKey(currrentLabel)){
				labelCount.put(currrentLabel, labelCount.get(currrentLabel)+1);
			}else{
				labelCount.put(currrentLabel, 1);
			}
		}
		
		for(String key : labelCount.keySet()){
			Integer count = labelCount.get(key);
			if(count > max){
				max = count;
				reuslt = key;
			}
		}
		return reuslt;
	}

	/**
	 * @Title: splitDataSet
	 * @Description: 按照指定特征划分数据集
	 * @param dataSet 数据集
	 * @param axis 指定划分的列
	 * @param value 列指定的值
	 * @return
	 * @return SplitDataSet
	 * 返回数据集合数据集列所对应的名字
	 */
	public SplitDataSet splitDataSet(List<List<String>> dataSet, List<String> dataSetNames,Integer axis, String value){
		List<List<String>> resDataSet = new ArrayList<List<String>>();
		List<String> resNameSet = new ArrayList<String>();
		boolean flag = true;
		for(List<String> single : dataSet ){
			//根据值，和位置，抽取数据
			if(value.equals(single.get(axis))){
				List<String> split = single.subList(0, axis);
				List<String> split2 = single.subList(axis+1, single.size());
				List<String> afterSplit = new ArrayList<String>();
				afterSplit.addAll(split);
				afterSplit.addAll(split2);
				resDataSet.add(afterSplit);
				if(flag){
					List<String> splitNames = dataSetNames.subList(0, axis);
					List<String> splitNames2 = dataSetNames.subList(axis+1, single.size());
					List<String> afterSplitNames = new ArrayList<String>();
					afterSplitNames.addAll(splitNames);
					afterSplitNames.addAll(splitNames2);
					resNameSet = afterSplitNames;
					flag = false;
				}
			}
		}
		SplitDataSet result = new SplitDataSet();
		result.setDataSet(resDataSet);
		result.setNameSet(resNameSet);
		return result;
	}
	
	/**
	 * @Title: chooseBestFeatureSplit
	 * @Description: 选择最好的划分方式
	 * 				   最好的划分方式的特征就是  信息增益=分类熵-信息熵，根据信息增益取最大的那个特征进行划分
	 * @param dataSet
	 * @return
	 * @return SplitResult
	 * 
	 */
	public SplitResult chooseBestFeatureSplit(List<List<String>> dataSet){
		SplitResult result = new SplitResult();
		Set<String> bestAttributes = new HashSet<String>();
		Integer bestFeature = -1;//最好的特征的序列
		double bestInfoGain = 0;//最优信息增益
		Integer numFeatures = dataSet.get(0).size()-1;//特征个数
		//当前分类熵
		double currentEntropy = calCategoryEnt(dataSet);
		//遍历所有特征
		for(int i=0;i<numFeatures;i++){
			//map存的是<特征值,<类别,类别个数>>，一个特征对应的不同类别和不同类别的个数
			Map<String,Map<String,Integer>> map = new HashMap<String,Map<String,Integer>>();
			Set<String> attributes = new HashSet<String>();
			//遍历每一条训练数据，匹配上面的map
			for(List<String> single : dataSet ){
				//每一条训练数据中的分类信息
				String currrentLabel = single.get(single.size()-1);
				//每一条训练数据指定元素特征信息
				String featureLabel = single.get(i);
				attributes.add(featureLabel);
				//先判断特征、再判断分类，增加num
				if(map.containsKey(featureLabel)){
					Map<String,Integer> currentMap = map.get(featureLabel);
					if(currentMap.containsKey(currrentLabel)){
						currentMap.put(currrentLabel, currentMap.get(currrentLabel)+1);
					}else{
						currentMap.put(currrentLabel, 1);
					}
				}else{
					Map<String,Integer> currentMap = new HashMap<String,Integer>();
					currentMap.put(currrentLabel, 1);
					map.put(featureLabel,currentMap);
				}
			}
			double comentropy = calComentropy(map, dataSet.size());
			double infoGain = currentEntropy - comentropy;
			if(infoGain > bestInfoGain){
				bestInfoGain = infoGain;
				bestFeature = i;
				bestAttributes = attributes;
			}
		}
		result.setIndex(bestFeature);
		List<String> listAttributes = new ArrayList<String> ();  
		listAttributes.addAll(bestAttributes);  
		result.setAttributes(listAttributes);
		result.setInfoGain(bestInfoGain);
		return result;
	}
	
	/**
	 * @Title: calEntropy
	 * @Description: 熵的计算
	 * @param map
	 * @param total
	 * @return
	 * @return double
	 * 概率*log2(概率)递减
	 */
	public double calEntropy(Map<String,Integer> map,Integer total){
		double entropy = 0;
		//计算熵
		//概率*log2(概率)递减
		for(String key : map.keySet()){
			double prob = (double)map.get(key)/total;
			entropy -= prob * Log.log2(prob);
		}
		return entropy;
	}
	
	/**
	 * @Title: calComentropy
	 * @Description: 计算特征熵,由于一个特征有不同的表现，所以对不同的表现进行加权处理
	 *               特征熵=特征值出现的概率*此特征值下的熵进行累加
	 *               H(C|T) = P(ti)*H(C|ti)的累加
	 * @param map
	 * @return
	 * @return double
	 */
	public double calComentropy(Map<String,Map<String,Integer>> map,Integer total){
		double comentropy = 0;
		for(String featureKey : map.keySet()){
			Map<String,Integer> currentMap = map.get(featureKey);
			Integer currentTotal = 0;
			for(String currentKey : currentMap.keySet()){
				currentTotal += currentMap.get(currentKey);
			}
			double entropy = calEntropy(currentMap, currentTotal);
			double prob = (double)currentTotal/total;
			comentropy += entropy * prob;
		}
		return comentropy;
	}
	
	/**
	 * @Title: testMapper
	 * @Description: 测试数据与名称做映射
	 * @param nameSet
	 * @param test
	 * @return
	 * @return Map<String,String>
	 */
	public Map<String,String> testMapper(List<String> nameSet, List<String> test){
		
		if(nameSet.size()- test.size() == 1){
			Map<String,String> result = new HashMap<String,String>();
			for(int i=0;i<test.size();i++){
				result.put(nameSet.get(i), test.get(i));
			}
			return result;
		}else{
			return null;
		}
	}
	
	/**
	 * @Title: getResult
	 * @Description: 得到结果
	 * @param root
	 * @param test
	 * @return void
	 */
	public String getResult(TreeNode root, Map<String,String> test){
		List<TreeNode> list = root.getChildren();
		String name = root.getName();
		if(list != null){
			for(TreeNode node : list){
				if(test.get(name).equals(node.getParentAttribute())){
					name = getResult(node, test);
				}
			}
		}
		return name;
	}
	
}
