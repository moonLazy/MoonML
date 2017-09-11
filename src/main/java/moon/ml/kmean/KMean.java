package moon.ml.kmean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import moon.ml.util.Matrix;


/**
 * @ClassName KMean
 * @Description K均值算法，适用于数值型特征的聚类
 * @author "liumingxin"
 * @Date 2017年9月11日 下午1:22:42
 * @version 1.0.0
 */
public class KMean {

	/**
	 * 读取测试文档，转换为矩阵
	 */
	public RealMatrix readDataSet(String fileIn){
		RealMatrix dataMat = null;
		List<List<Double>> dataList = new ArrayList<List<Double>>();
		try {
			File file = new File(fileIn);
			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			String line = null;
			while ((line = in.readLine()) != null) {
				String[] mArray = line.split("\t");
				List<Double> data = new ArrayList<Double>();
				for(int i=0;i<mArray.length;i++){
					data.add(Double.parseDouble(mArray[i]));
				}
				dataList.add(data);
			}
			
			//转换为矩阵
			int m = dataList.size();
			int n = dataList.get(0).size();
			double[][] dataArray = new double[m][n];
			for(int i=0;i<dataList.size();i++){
				List<Double> datas = dataList.get(i);
				for(int j=0;j<datas.size();j++){
					dataArray[i][j] = datas.get(j);
				}
			}
			
			dataMat = new Array2DRowRealMatrix(dataArray);
			
			
			in.close();
			reader.close();
			
		} catch (Exception e) {
			System.out.println("读取出错");
			e.printStackTrace();
		}
		return dataMat;
	}
	
	/**
	 * @Title: maxAndMinByColumn
	 * @Description: 找出每一列的最大值最小值及最大最小的差
	 * @param dataMat
	 * @return
	 * @return Map<String,double[]>
	 */
	public Map<String,RealVector> maxMinAndDifferenceByColumn(RealMatrix dataMat){
		Map<String,RealVector> map = new HashMap<String,RealVector>();
		int columnCount = dataMat.getColumnDimension();
		double[] max = new double[columnCount];
		double[] min = new double[columnCount];
		double[] difference = new double[columnCount];
		for(int i=0;i<columnCount;i++){
			max[i] = dataMat.getColumnVector(i).getMaxValue();
			min[i] = dataMat.getColumnVector(i).getMinValue();
			difference[i] = max[i] - min[i];
		}
		RealVector maxV = new ArrayRealVector(max);
		RealVector minV = new ArrayRealVector(min);
		RealVector differenceV = new ArrayRealVector(difference);
		map.put("max", maxV);
		map.put("min", minV);
		map.put("difference", differenceV);
		
		return map;
		
	}
	
	/**
	 * @Title: randCent
	 * @Description: 随机寻找簇质心
	 * @param map
	 * @param k
	 * @return
	 * @return RealMatrix
	 */
	public RealMatrix randCent(Map<String,RealVector> map,int k){
		RealVector min = map.get("min");
		RealVector difference = map.get("difference");
		Random random = new Random();
		double[][] d = new double[k][min.getDimension()];
		for(int i=0;i<k;i++){
			double rand = random.nextDouble();
			RealVector vec = min.add(difference.mapMultiply(rand));
			d[i] = vec.toArray();
		}
		RealMatrix centerRoids = new Array2DRowRealMatrix(d);
		return centerRoids;
	}
	
	/**
	 * @Title: vectorDistance
	 * @Description: 计算两向量之间的距离
	 * @param a
	 * @param b
	 * @return
	 * @return double
	 */
	public double vectorDistance(RealVector a, RealVector b){
		return a.getDistance(b);
	}
	
	/**
	 * @Title: kmeans
	 * @Description: K均值算法
	 * @param data
	 * @param k
	 * @return
	 * @return List<KMeanResult>
	 */
	public List<KMeanResult> kmeans(RealMatrix data, int k){
		
		int row = data.getRowDimension();
		
		//找最大最小及差距
		Map<String,RealVector> map = maxMinAndDifferenceByColumn(data);
		//随机寻找质心
		RealMatrix centroid = randCent(map, k);
		
		//创建row行2列的-1矩阵，用于存储簇的索引和误差（当前点到簇质心的距离的平方）
		RealMatrix clusterAssment = Matrix.minusOnes(row, 2);
		
		boolean clusterChanged = true;
		
		//质心序列与簇成员向量和映射
		Map<Integer,RealVector> summapper = null;
		//质心序列与簇成员向量映射
		Map<Integer,List<DistanceVecotrMapper>> membersmapper = null;
		//质心序列与簇成员向量个数映射
		Map<Integer,Integer> nummapper = null;
		
		
		while(clusterChanged){
			clusterChanged = false;
			
			//循环所有数据
			for(int i=0;i<row;i++){
				//当前行向量
				RealVector thisVector = data.getRowVector(i);
				//当前质心向量
				RealVector thisCentroidVector = centroid.getRowVector(0);
				//默认第一个质心的序列
				int minIndex = 0;
				//默认到第一个质心的距离为最小距离
				double minDist = vectorDistance(thisVector, thisCentroidVector);
				for(int j=1;j<k;j++){
					//第I条数据向量到第J个质心的距离
					double distJI = vectorDistance(thisVector, centroid.getRowVector(j));
					//寻找最近质心
					if(distJI < minDist){
						minDist = distJI;
						minIndex = j;
					}
				}
				//质心发生改变
				if(clusterAssment.getEntry(i, 0) != minIndex){
					clusterChanged = true;
					clusterAssment.setEntry(i, 0, minIndex);
					clusterAssment.setEntry(i, 1, Math.pow(minDist, 2));
				}
			}
			//质心序列与簇成员向量和映射
			summapper = new HashMap<Integer,RealVector>();
			//质心序列与簇成员向量映射
			membersmapper = new HashMap<Integer,List<DistanceVecotrMapper>>();
			//质心序列与簇成员向量个数映射
			nummapper = new HashMap<Integer,Integer>();
			
			//计算各簇中所有向量和及个数，把质心序列与簇成员向量映射起来
			for(int i=0;i<row;i++){
				int rentroidIndex = (int)clusterAssment.getEntry(i, 0);
				RealVector r = summapper.get(rentroidIndex);
				
				if(r == null){
					summapper.put(rentroidIndex, data.getRowVector(i));
					nummapper.put(rentroidIndex, 1);
				}else{
					summapper.put(rentroidIndex, r.add(data.getRowVector(i)));
					nummapper.put(rentroidIndex, nummapper.get(rentroidIndex)+1);
				}
				
				List<DistanceVecotrMapper> members = membersmapper.get(rentroidIndex);
				if(members == null){
					members = new ArrayList<DistanceVecotrMapper>();
				}
				DistanceVecotrMapper dvMapper = new DistanceVecotrMapper();
				dvMapper.setVector(data.getRowVector(i));
				dvMapper.setDistance(clusterAssment.getEntry(i, 1));
				members.add(dvMapper);
				membersmapper.put(rentroidIndex, members);
			}
			
			//计算簇中所有向量平均值作为质心，这也是为什么叫k-mean中的mean的解释
			for(int i=0;i<k;i++){
				RealVector r = summapper.get(i);
				if(r != null){
					RealVector mean = r.mapDivide(nummapper.get(i));
					centroid.setRowVector(i, mean);
				}
			}
		}
		
		List<KMeanResult> result = new ArrayList<KMeanResult>();
		for(Integer key : membersmapper.keySet()){
			KMeanResult r = new KMeanResult();
			r.setCentroidIndex(key);
			r.setCentroid(centroid.getRowVector(key));
			r.setClusterMembers(membersmapper.get(key));
			result.add(r);
		}
		return result;
	}
	
	/**
	 * @Title: biKmeans
	 * @Description: 二分K均值（解决K均值算法收敛到局部最小的问题）
	 * @param data
	 * @param k
	 * @return
	 * @return List<KMeanResult>
	 */
	public List<KMeanResult> biKmeans(RealMatrix data, int k){
		int row = data.getRowDimension();
		//创建row行2列的-1矩阵，用于存储簇的索引和误差（当前点到簇质心的距离的平方）
		//RealMatrix clusterAssment = Matrix.minusOnes(row, 2);
		//全部数据的矩阵计算平均向量作为其实质心
		RealVector centroid0 = calMeanVector(data);
		KMeanResult firstResult = new KMeanResult();
		firstResult.setCentroid(centroid0);
		firstResult.setCentroidIndex(0);
		List<DistanceVecotrMapper> firstResultMembers = new ArrayList<DistanceVecotrMapper>();
		//所有数据与第一个质心之间的SSE
		for(int i=0;i<row;i++){
			//当前行向量
			RealVector thisVector = data.getRowVector(i);
			double minDist = vectorDistance(thisVector, centroid0);
			double thisSSE = Math.pow(minDist, 2);
			//clusterAssment.setEntry(i, 1, thisSSE);
			DistanceVecotrMapper mapper = new DistanceVecotrMapper();
			mapper.setDistance(thisSSE);
			mapper.setVector(thisVector);
			firstResultMembers.add(mapper);
		}
		firstResult.setClusterMembers(firstResultMembers);
		
		List<KMeanResult> results = new ArrayList<KMeanResult>();
		results.add(firstResult);
		
		//最小残差平方和
		double lowestSSE = calSSE(firstResult);
		//被划分的序列
		int bestCentToSplit = 0;
		//划分后的簇集合
		List<KMeanResult> bestResultToSplit = null;
		
		while(results.size() < k){
			//获取每个簇的信息 二分划分每一个簇
			for(int i=0;i<results.size();i++){
				KMeanResult kMeanResult = results.get(i);
				List<DistanceVecotrMapper> mappers = kMeanResult.getClusterMembers();
				//double sse = calSSE(kMeanResult);
				int clusterRow = mappers.size();
				int clusterColumn = mappers.get(0).getVector().toArray().length;
				RealMatrix cluster = new Array2DRowRealMatrix(clusterRow,clusterColumn);
				for(int j=0;j<mappers.size();j++){
					DistanceVecotrMapper mapper = mappers.get(j);
					cluster.setRowVector(j, mapper.getVector());
				}
				//二分划分每一个簇
				List<KMeanResult> splitResults = kmeans(cluster, 2);
				double sumsse = 0;
				for(KMeanResult splitResult : splitResults){
					sumsse += calSSE(splitResult);
				}
				//找出残差平方和降低到最小的簇信息及划分后的簇
				if(sumsse < lowestSSE){
					lowestSSE = sumsse;
					bestCentToSplit = i;
					bestResultToSplit = splitResults;
				}
			}
			//加入结果集中
			if(bestResultToSplit != null){
				results.remove(bestCentToSplit);
				results.addAll(bestResultToSplit);
				for(int i=0;i<results.size();i++){
					results.get(i).setCentroidIndex(i);
				}
			}
			
		}
		return results;
	}
	
	/**
	 * @Title: calMeanVector
	 * @Description: 计算矩阵的平均向量
	 * @param data
	 * @return
	 * @return RealVector
	 */
	public RealVector calMeanVector(RealMatrix data){
		int row = data.getRowDimension();
		int column = data.getColumnDimension();
		RealVector sum = new ArrayRealVector(column);
		for(int i=0;i<row;i++){
			sum = sum.add(data.getRowVector(i));
		}
		return sum.mapDivide(row);
	}
	
	/**
	 * @Title: calSSE
	 * @Description: 计算簇到质心的残差平方和
	 * @param data
	 * @param centroid
	 * @return
	 * @return double
	 */
	public double calSSE(RealMatrix data, RealVector centroid){
		double sumSSE = 0;
		int row = data.getRowDimension();
		for(int i=0;i<row;i++){
			//当前行向量
			RealVector thisVector = data.getRowVector(i);
			double minDist = vectorDistance(thisVector, centroid);
			double thisSSE = Math.pow(minDist, 2);
			sumSSE += thisSSE;
		}
		return sumSSE;
	}
	
	/**
	 * @Title: calSSE
	 * @Description: 计算簇到质心的残差平方和
	 * @param kMeanResult
	 * @return
	 * @return double
	 */
	public double calSSE(KMeanResult kMeanResult){
		double sumSSE = 0;
		for(DistanceVecotrMapper mapper :kMeanResult.getClusterMembers()){
			sumSSE += mapper.getDistance();
		}
		return sumSSE;
	}
	
	/**
	 * @Title: calTotalSSE
	 * @Description: 计算总残差平方和
	 * @param list
	 * @return
	 * @return double
	 */
	public double calTotalSSE(List<KMeanResult> list){
		double sumSSE = 0;
		for(KMeanResult result : list){
			sumSSE += calSSE(result);
		}
		return sumSSE;
	}
	
	/**
	 * @Title: train
	 * @Description: 训练选取最优结果
	 * @param data 训练集
	 * @param k	质心个数
	 * @param maxIterations	最大迭代次数
	 * @return	质心的坐标
	 * @return List<KMeanResult>
	 */
	public List<KMeanResult> train(RealMatrix data, int k, int maxIterations){
		List<KMeanResult> lowestKmeanResult = kmeans(data, k);
		double lowestKMeanSSE = calTotalSSE(lowestKmeanResult);
		List<KMeanResult> lowestBiKmeanResult = biKmeans(data, k);
		double lowestBiKmeanSSE = calTotalSSE(lowestBiKmeanResult);
		double kMeanSSE,biKmeanSSE;
		for(int i=0;i<maxIterations;i++){
			List<KMeanResult> kmeanResult = kmeans(data, k);
			kMeanSSE = calTotalSSE(kmeanResult);
			if(lowestKMeanSSE > kMeanSSE){
				lowestKMeanSSE = kMeanSSE;
				lowestKmeanResult = kmeanResult;
			}
			
			
			List<KMeanResult> biKmeanResult = biKmeans(data, k);
			biKmeanSSE = calTotalSSE(biKmeanResult);
			if(lowestBiKmeanSSE > biKmeanSSE){
				lowestBiKmeanSSE = biKmeanSSE;
				lowestBiKmeanResult = biKmeanResult;
			}
		}
		return lowestKMeanSSE > lowestBiKmeanSSE ? lowestBiKmeanResult : lowestKmeanResult;
	}
	
	public static void main(String[] args) {
		KMean kmean = new KMean();
		String dataSetFilePath = "data/kmeans/testSet.txt";
		//读取数据集
		RealMatrix data = kmean.readDataSet(dataSetFilePath);
		int maxIterations = 200;
		List<KMeanResult> result = kmean.train(data, 4, maxIterations);
		for(int i=0;i<result.size();i++){
			KMeanResult r = result.get(i);
			System.out.println("==========="+i+"的质心的============"+r.getCentroid().toArray()[0]+"   "+r.getCentroid().toArray()[1]);
			System.out.println("==========="+i+"的第0列============");
			List<DistanceVecotrMapper> list = r.getClusterMembers();
			for(DistanceVecotrMapper d : list){
				double[] dd = d.getVector().toArray();
				System.out.println(dd[0]);
			}
			System.out.println("=======================");
		}
		for(int i=0;i<result.size();i++){
			KMeanResult r = result.get(i);
			System.out.println("==========="+i+"的第1列============");
			List<DistanceVecotrMapper> list = r.getClusterMembers();
			for(DistanceVecotrMapper d : list){
				double[] dd = d.getVector().toArray();
				System.out.println(dd[1]);
			}
			System.out.println("=======================");
		}
	}

}
