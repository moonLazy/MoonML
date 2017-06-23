package moon.ml.record;

/**
 * @ClassName DistanceMapperString
 * @Description 训练数据与带分类数据的距离-数据映射,data为String类型
 * @author "liumingxin"
 * @Date 2017年5月22日 下午3:54:37
 * @version 1.0.0
 */
public class DistanceMapperString implements Comparable<DistanceMapperString> {
	private RecordWithFeaturesString data;
	private Double distance;

	public RecordWithFeaturesString getData() {
		return data;
	}

	public void setData(RecordWithFeaturesString data) {
		this.data = data;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int compareTo(DistanceMapperString that) {
		return this.getDistance().compareTo(that.getDistance());
	}

}
