package moon.ml.decisiontree;

import java.util.List;

public class SplitResult {
	private Integer index;//数组中的序列
	private double infoGain;//信息增益
	private List<String> attributes;//属性
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public double getInfoGain() {
		return infoGain;
	}
	public void setInfoGain(double infoGain) {
		this.infoGain = infoGain;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	@Override
	public String toString() {
		return "SplitResult [index=" + index + ", infoGain=" + infoGain + ", attributes=" + attributes + "]";
	}
}
