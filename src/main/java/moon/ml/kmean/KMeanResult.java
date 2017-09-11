package moon.ml.kmean;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;

public class KMeanResult {
	private Integer centroidIndex;
	private RealVector centroid;
	private List<DistanceVecotrMapper> clusterMembers;
	public Integer getCentroidIndex() {
		return centroidIndex;
	}
	public void setCentroidIndex(Integer centroidIndex) {
		this.centroidIndex = centroidIndex;
	}
	public RealVector getCentroid() {
		return centroid;
	}
	public void setCentroid(RealVector centroid) {
		this.centroid = centroid;
	}
	public List<DistanceVecotrMapper> getClusterMembers() {
		return clusterMembers;
	}
	public void setClusterMembers(List<DistanceVecotrMapper> clusterMembers) {
		this.clusterMembers = clusterMembers;
	}
	@Override
	public String toString() {
		return "KMeanResult [centroidIndex=" + centroidIndex + ", centroid=" + centroid + ", clusterMembers="
				+ clusterMembers + "]";
	}
	
	
}
