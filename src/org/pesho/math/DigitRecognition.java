package org.pesho.math;

import java.util.Arrays;
import java.util.List;

public class DigitRecognition {
	
	public int recognize(List<RecordItem> record) {
		return 0;
	}

	public int getCurvesCount(List<RecordItem> record) {
		int count = 0;
		for (RecordItem item: record) {
			if (item.getAction() == 0) count++;
		}
		return count;
	}
	
	public char[][] drawMatrix(List<RecordItem> record) {
		int w = 20;
		int h = 30;
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
	
}
