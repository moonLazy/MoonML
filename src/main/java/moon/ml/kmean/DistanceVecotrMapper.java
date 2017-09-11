package moon.ml.kmean;

import org.apache.commons.math3.linear.RealVector;

public class DistanceVecotrMapper {
	private double distance;
	private RealVector vector;
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public RealVector getVector() {
		return vector;
	}
	public void setVector(RealVector vector) {
		this.vector = vector;
	}
	@Override
	public String toString() {
		return "DistanceVecotrMapper [distance=" + distance + ", vector=" + vector + "]";
	}
}
