package moon.ml.knearestneighbor;

/**
 * @ClassName ResultMapper
 * @Description 结果信息
 * @author "liumingxin"
 * @Date 2017年5月22日 下午4:30:04
 * @version 1.0.0
 */
public class ResultInfo implements Comparable<ResultInfo>{
	private Object classify;
	private Integer frequency = 0;//频次
	private Double totalDistance = 0d;
	public Object getClassify() {
		return classify;
	}
	public void setClassify(Object classify) {
		this.classify = classify;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	public Double getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}
	
	public int compareTo(ResultInfo that) {
		if(this.getFrequency() == this.getFrequency()){
			return this.getTotalDistance().compareTo(that.getTotalDistance());
		}
		return this.getFrequency().compareTo(that.getFrequency());
	}
	
}
