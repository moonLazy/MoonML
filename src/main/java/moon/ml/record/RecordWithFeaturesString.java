package moon.ml.record;

import java.util.List;

/**
 * @ClassName RecordWithFeaturesString
 * @Description 特征值为字符串类型的记录
 * @author "liumingxin"
 * @Date 2017年6月20日 下午1:57:28
 * @version 1.0.0
 */
public class RecordWithFeaturesString {
	private List<String> features;//特征集合
	private Object category;//类别
	
	public List<String> getFeatures() {
		return features;
	}
	public void setFeatures(List<String> features) {
		this.features = features;
	}
	public Object getCategory() {
		return category;
	}
	public void setCategory(Object category) {
		this.category = category;
	}
}
