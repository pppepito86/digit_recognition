package org.pesho.math;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class DigitRecognition {
	
	public float dotProd(float[] a, float[] b) {
		float res = 0;
		for(int i = 0; i < a.length; i++) {
			res += a[i]*b[i];
		}
		return res;
	}
	
	public int recognize(List<RecordItem> record) throws Exception {
		float vectors[][] = train();
		
		float v[] = new float[15];
		int cols[] = getColumns(record, 150);
//		System.out.println(Arrays.toString(cols));
//		float power[] = new float[15];
		
		for(int i : cols) v[i]++;
		float total = 0;
		for(float i : v) total += i;
		
		for(int i = 0; i < v.length; i++) {
			v[i] /= total;
		}
		
		float dots[] = new float[10];
		for(int i = 0; i < dots.length; i++) {
			dots[i] = dotProd(vectors[i], v);
		}
		
		float minVal = 1e8f;
		int minPos = -1;
		
		for(int i = 0; i < dots.length; i++) {
			if(dots[i] < minVal) {
				minVal = dots[i];
				minPos = i;
			}
		}
		
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
	
	public float[][] train() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		File dir = new File("train");
		File[] files = dir.listFiles();
		final int res = 150;
		
		float vectors[][] = new float[10][15];
		
		for(File file : files) {
			int dig = file.getName().charAt(0) - '0';
			//System.out.println(file.getName());
			
			List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
			
			DigitRecognition digitRecognition = new DigitRecognition();
			int cols[] = digitRecognition.getColumns(record, res);
//			System.out.println(Arrays.toString(cols));
//			float power[] = new float[15];
			
			for(int i : cols) vectors[dig][i]++;
			float total = 0;
			for(float i : vectors[dig]) total += i;
			
			for(int i = 0; i < vectors[dig].length; i++) {
				vectors[dig][i] /= total;
			}
		}
		
		return vectors; 
	}
	
}
