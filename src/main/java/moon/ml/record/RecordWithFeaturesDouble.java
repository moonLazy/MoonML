package moon.ml.record;

import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * @ClassName RecordWithFeaturesDouble
 * @Description 特征值为double类型的记录
 * @author "liumingxin"
 * @Date 2017年6月20日 下午1:57:52
 * @version 1.0.0
 */
public class RecordWithFeaturesDouble{
	private List<Double> features;//特征集合
	private Object category;//类别
	public List<Double> getFeatures() {
		return features;
	}
	public void setFeatures(List<Double> features) {
		this.features = features;
	}
	public Object getCategory() {
		return category;
	}
	public void setCategory(Object category) {
		this.category = category;
	}
	
	/**
	 * @Title: featuresList2RealVector
	 * @Description: 特征集合转换成特征向量
	 * @return
	 * @return RealVector
	 */
	public RealVector featuresList2RealVector(){
		RealVector vector = new ArrayRealVector(this.features.size());
		for(int i=0;i<this.features.size();i++){
			vector.addToEntry(i, this.features.get(i));
		}
		return vector;
	}
}
