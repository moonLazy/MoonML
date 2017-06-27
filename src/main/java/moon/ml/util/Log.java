package moon.ml.util;

/**
 * @ClassName Log
 * @Description 对数操作工具类
 * @author "liumingxin"
 * @Date 2017年5月16日 下午2:01:13
 * @version 1.0.0
 */
public class Log {

	/**
	 * @Title: log
	 * @Description: 计算以base为底，value的对数
	 * @param value
	 * @param base 底数
	 * @return
	 * @return double
	 */
	public static double log(double value, double base){
		return Math.log(value) / Math.log(base);
	}
	
	/**
	 * @Title: log2
	 * @Description: 计算以2为底，value的对数
	 * @param value
	 * @return
	 * @return double
	 */
	public static double log2(double value){
		return log(value, 2);
	}
	
	/**
	 * @Title: log10
	 * @Description: 计算以10为底，value的对数
	 * @param value
	 * @return
	 * @return double
	 */
	public static double log10(double value){
		return Math.log10(value);
	}
	
	/**
	 * @Title: loge
	 * @Description: 计算以e为底，value的对数
	 * @param value
	 * @return
	 * @return double
	 */
	public static double loge(double value){
		return Math.log(value);
	}
	
	public static void main(String[] args) {
		double entropy = log2(1);
		System.out.println(entropy);

	}

}
