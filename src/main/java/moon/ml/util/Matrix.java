package moon.ml.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @ClassName CreateMatrix
 * @Description 创建矩阵通用方法，这里创建的是0，1矩阵
 * @author "liumingxin"
 * @Date 2017年6月5日 上午10:51:43
 * @version 1.0.0
 */
public class Matrix {

	/**
	 * @Title: zeros
	 * @Description: r*c矩阵元素全为0
	 * @param rowNum
	 * @param columnNum
	 * @return
	 * @return RealMatrix
	 */
	public static RealMatrix zeros(int rowNum, int columnNum){
		double b [][] = new double[rowNum][columnNum];
        for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b[0].length; j++){
            	b[i][j] = 0;
            }
        }
        //将数组转化为矩阵
        RealMatrix matrix = new Array2DRowRealMatrix(b);
        return matrix;
	}
	
	/**
	 * @Title: ones
	 * @Description: r*c矩阵元素全为1
	 * @param rowNum
	 * @param columnNum
	 * @return
	 * @return RealMatrix
	 */
	public static RealMatrix ones(int rowNum, int columnNum){
		double b [][] = new double[rowNum][columnNum];
		for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b[0].length; j++){
            	b[i][j] = 1;
            }
        }
        //将数组转化为矩阵
        RealMatrix matrix = new Array2DRowRealMatrix(b);
        return matrix;
	}
	/**
	 * @Title: ones
	 * @Description: r*c矩阵元素全为-1
	 * @param rowNum
	 * @param columnNum
	 * @return
	 * @return RealMatrix
	 */
	public static RealMatrix minusOnes(int rowNum, int columnNum){
		double b [][] = new double[rowNum][columnNum];
		for(int i = 0; i < b.length; i++) {
            for(int j = 0; j < b[0].length; j++){
            	b[i][j] = -1;
            }
        }
        //将数组转化为矩阵
        RealMatrix matrix = new Array2DRowRealMatrix(b);
        return matrix;
	}
	
	/**
	 * @Title: multiply
	 * @Description: 同行、同列矩阵各元素相乘
	 * @param a
	 * @param b
	 * @return
	 * @return RealMatrix
	 */
	public static RealMatrix multiply(RealMatrix a, RealMatrix b){
		if(a.getRowDimension() == b.getRowDimension() && a.getColumnDimension() == b.getColumnDimension()){
			int r = a.getRowDimension();
			int c = a.getColumnDimension();
			double[][] aa = a.getData();
			double[][] bb = b.getData();
			double[][] cc = new double[r][c];
			for(int i = 0; i < r; i++) {
	            for(int j = 0; j < c; j++){
	            	cc[i][j] = aa[i][j] * bb[i][j];
	            }
	        }
			return new Array2DRowRealMatrix(cc);
		}
		return null;
	}
	
	/**
	 * @Title: realMatrix2ListDouble
	 * @Description: 实数矩阵转成List<Double>
	 * @param dataRealMatrix
	 * @return
	 * @return List<Double>
	 */
	public static List<Double> realMatrix2ListDouble(RealMatrix dataRealMatrix){
		if(dataRealMatrix == null){
			return null;
		}
		List<Double> dataList = new ArrayList<Double>();
		int row = dataRealMatrix.getRowDimension();
		for(int i=0;i<row;i++){
			double[] ds = dataRealMatrix.getRow(i);
			for(int j=0;j<ds.length;j++){
				dataList.add(ds[j]);
			}
		}
		return dataList;
	}
}
