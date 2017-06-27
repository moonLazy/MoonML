package moon.ml.util;

import java.util.List;

/**
 * @ClassName vector
 * @Description 向量计算
 * @author "liumingxin"
 * @Date 2017年5月23日 上午9:28:54
 * @version 1.0.0
 */
public class VectorMethod {

	/**
	 * @Title: calCos
	 * @Description: 向量夹角余弦值(x1*x2+y1*y2+z1*z2)/(normA + normB)
	 * @param a
	 * @param b
	 * @return
	 * @return Double
	 */
	public static Double calCos(List<Double> a,List<Double> b){
		if(a.size() != b.size()){
			return null;
		}
		Double normA = calNorm(a);
		Double normB = calNorm(b);
		Double sum = 0d;
		for(int i=0;i<a.size();i++){
			sum += a.get(i) * b.get(i);
		}
		return sum / (normA + normB);
	}
	
	public static Double calCos(double[] a,double[] b){
		if(a.length != b.length){
			return null;
		}
		Double normA = calNorm(a);
		Double normB = calNorm(b);
		Double sum = 0d;
		for(int i=0;i<a.length;i++){
			sum += a[i] * b[i];
		}
		return sum / (normA + normB);
	}
	
	/**
	 * @Title: calNorm
	 * @Description: 求向量的模长
	 * @param a
	 * @return
	 * @return Double
	 */
	public static Double calNorm(List<Double> a){
		Double sum = 0d;
		for(Double d : a){
			sum += Math.pow(d, 2);
		}
		return Math.sqrt(sum);
	}
	
	public static Double calNorm(double[] a){
		Double sum = 0d;
		for(Double d : a){
			sum += Math.pow(d, 2);
		}
		return Math.sqrt(sum);
	}
	
	/**
	 * @Title: calEuclideanMetric
	 * @Description: 计算欧式距离：[(x1-x2)^2+(y1-y2)^2]开根
	 * @param a
	 * @param b
	 * @return
	 * @return Double
	 */
	public static Double calEuclideanMetric(List<Double> a, List<Double> b){
		if(a.size() != b.size()){
			return null;
		}
		Double sum = 0d;
		for(int i=0;i<a.size();i++){
			sum += Math.pow((a.get(i)-b.get(i)), 2);
		}
		return Math.sqrt(sum);
	}
	
	public static Double calEuclideanMetric(double[] a, double[] b){
		if(a.length != b.length){
			return null;
		}
		Double sum = 0d;
		for(int i=0;i<a.length;i++){
			sum += Math.pow((a[i] - b[i]), 2);
		}
		return Math.sqrt(sum);
	}
}
