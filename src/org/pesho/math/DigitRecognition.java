package org.pesho.math;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class DigitRecognition {
	private List<Sample> samples;
	
	public DigitRecognition() {
		try {
			samples = train();
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * mat[i][j] = the probability of the real character being j if the algorithms outputs i 
	 * @return
	 * @throws Exception 
	 */
	public float[][] getProbabilityMatrix() throws Exception {
		File dir = new File("data");
		File[] files = dir.listFiles();
		
		
		int[][] occurences = new int[10][10];
		
		for(File file : files) {
			int expected = file.getName().charAt(0) - '0';
			List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
			int real = recognize(record);
			
			occurences[real][expected] ++;
		}	
		
		for(int i = 0; i < 10; i++) occurences[i][i] -= samples.size()/10;
		
		float matrix[][] = new float[10][10];
		
		for(int i = 0; i < 10; i++) {
			int sum = Arrays.stream(occurences[i]).sum();
			if(sum != 0)
				for(int j = 0; j < 10; j++) {
					matrix[i][j] = occurences[i][j] / (float) sum;
				}
		}
		
		return matrix;
	}
	
	public float dotProd(float[] a, float[] b) {
		float res = 0;
		for(int i = 0; i < a.length; i++) {
			res += a[i]*b[i];
		}
		return res;
	}
	
	public int recognize(List<RecordItem> record) throws Exception {
		
		
		float v[] = new float[15];
		
		v = getTraitsVector(record);
		
		
		float minVal =  -1;
		int minPos = -1;
		
		
		for(int i = 0; i < samples.size(); i++) {
			float dot = dotProd(samples.get(i).vector, v);
			
			if(dot > minVal) {
				minVal = dot;
				minPos = samples.get(i).label;
			}
		}
		
//		float best = minVal;
//		minVal = -1;
//		
//		for(int i = 0; i < samples.size(); i++) {
//			float dot = dotProd(samples.get(i).vector, v);
//			
//			if(dot != best && dot > minVal) {
//				minVal = dot;
//				minPos = samples.get(i).label;
//			}
//		}


		
		return minPos;
	}
	
	

	public int getCurvesCount(List<RecordItem> record) {
		int count = 0;
		for (RecordItem item: record) {
			if (item.getAction() == 0) count++;
		}
		return count;
	}
	
	public char[][] drawMatrix(List<RecordItem> record) {
		int w = 200;
		int h = 300;
		char[][] mat = new char[h][w];
		for(int i = 0; i < mat.length; i++)
			Arrays.fill(mat[i], ' ');
		
		
		int offsetX, offsetY;
		offsetX = 0;//200;
		offsetY = 0;//100;
		
		for(RecordItem item : record) {
			int x = (int) (item.getX()*w - offsetX);
			int y = (int) (item.getY()*h - offsetY);
			if(x < w && y < h)
				mat[y][x] = 'x';
		}
		
//		for(int i = 0; i < mat.length; i++)
//			System.out.println(mat[i]);
		
		return mat;
	}
	
	public int[] getColumns(List<RecordItem> record, int numCols) {
		int [] cols = new int[numCols];
		
		float width = 1 / (float)(numCols+1);
		
		for(int i = 1; i <= numCols; i++) {
			float col = i*width;
			
			for(int j = 1; j < record.size(); j++) {
				if(record.get(j-1).getAction() == 1) continue;
				
				if(record.get(j).getX() < col && col <= record.get(j-1).getX() ) {
					cols[i-1]++;
				} else if(record.get(j).getX() > col && col >= record.get(j-1).getX() ) {
					cols[i-1]++;
				}
				
			}
		}
		
		
		return cols;
	}
	
	public int[] getRows(List<RecordItem> record, int numRows) {
		int [] rows = new int[numRows];
		
		float width = 1 / (float)(numRows+1);
		
		for(int i = 1; i <= numRows; i++) {
			float row = i*width;
			
			for(int j = 1; j < record.size(); j++) {
				if(record.get(j-1).getAction() == 1) continue;
				
				if(record.get(j).getY() < row && row <= record.get(j-1).getY() ) {
					rows[i-1]++;
				} else if(record.get(j).getY() > row && row >= record.get(j-1).getY() ) {
					rows[i-1]++;
				}
				
			}
		}
		
		
		return rows;
	}
	
	/**
	 * res[i] = number of occurences of i in arr;
	 */
	public int[] getCounts(int[] arr) { 
		final int maxElement = 15;
		int res[] = new int[maxElement];
		for(int i : arr) res[i]++;
		return res;
	}
	
	public float[] normalize(int[] vector) {
		float[] normal = new float[vector.length];
		
		float len = 0;
		for(int i : vector) len += i*i;
		len = (float)Math.sqrt(len);
		
		for(int i = 0; i < normal.length; i++) {
			normal[i] = vector[i] / len;
		}
			
		return normal;
	}
	
	public float[] getTraitsVector(List<RecordItem> record) {
		float vector[];
		final int res = 150;
		
		int cols[] = getColumns(record, res);
		int[] colCounts = getCounts(cols);
		colCounts[0] = 0;
		float colNormal[] = normalize(colCounts);
		
		int rows[] = getRows(record, res);
		int[] rowCounts = getCounts(rows);
		rowCounts[0] = 0;
		float rowNormal[] = normalize(rowCounts);
		
		vector = new float[colNormal.length + rowNormal.length];
		int vecIdx = 0;
		for(float x : colNormal) vector[vecIdx++] = x;
		for(float x : rowNormal) vector[vecIdx++] = x;
		
		
		return vector;
	}
	
	
	public List<Sample> train() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		File dir = new File("train");
		File[] files = dir.listFiles();
		
//		float vectors[][] = new float[10][];
		
		List<Sample> samples = new ArrayList<Sample>();
		for(File file : files) {
			int dig = file.getName().charAt(0) - '0';
			
			List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
			float vector[] = getTraitsVector(record);
			samples.add(new Sample(dig, vector));
//			System.out.println(dig + " : " + Arrays.toString(vectors[dig]));
		}
		
		return samples; 
	}
	
}
