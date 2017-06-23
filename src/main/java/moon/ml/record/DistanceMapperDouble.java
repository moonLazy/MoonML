package moon.ml.record;

/**
 * @ClassName DoubleDistanceMapper
 * @Description 训练数据与带分类数据的相似程度-数据映射
 * @author "liumingxin"
 * @Date 2017年5月22日 下午3:54:37
 * @version 1.0.0
 */
public class DistanceMapperDouble implements Comparable<DistanceMapperDouble> {
	private RecordWithFeaturesDouble data;
	private Double distance;

	public RecordWithFeaturesDouble getData() {
		return data;
	}

	public void setData(RecordWithFeaturesDouble data) {
		this.data = data;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int compareTo(DistanceMapperDouble that) {
		return this.getDistance().compareTo(that.getDistance());
	}

}
