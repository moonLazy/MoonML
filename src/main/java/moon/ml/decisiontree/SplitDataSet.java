package moon.ml.decisiontree;

import java.util.List;

public class SplitDataSet {
	//数据集
	private List<List<String>> dataSet;
	//数据集列所对应的名称
	private List<String> nameSet;
	
	public List<List<String>> getDataSet() {
		return dataSet;
	}
	public void setDataSet(List<List<String>> dataSet) {
		this.dataSet = dataSet;
	}
	public List<String> getNameSet() {
		return nameSet;
	}
	public void setNameSet(List<String> nameSet) {
		this.nameSet = nameSet;
	}
	
}
