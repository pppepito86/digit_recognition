package org.pesho.math.test;

import java.awt.dnd.DragGestureEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.pesho.math.DigitRecognition;
import org.pesho.math.RecordItem;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class DigitRecognitionTest {
	@Ignore
	@Test
	public void testCurvesCount() throws Exception {
		DigitRecognition digitRecognition = new DigitRecognition();
		
		File dir = new File("data");
		File[] files = dir.listFiles();
		for(File file : files) {
			System.out.println(file.getName());
			int digit = file.getName().charAt(0) - '0';
			List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
			int real = digitRecognition.getCurvesCount(record);
			int expected = 1;
			if (file.getName().startsWith("40") || digit == 5 || digit == 7) expected ++;
			Assert.assertEquals(expected, real);
		}
	}
	
	
	@Test
	public void testAll() throws Exception {
		DigitRecognition digitRecognition = new DigitRecognition();
		
		File dir = new File("data");
		File[] files = dir.listFiles();
		
		int total[] = new int[10];
		int correct[] = new int[10];
		
		for(File file : files) {
			int expected = file.getName().charAt(0) - '0';
			List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
			int real = digitRecognition.recognize(record);
			
			total[expected]++;
			if(expected == real) correct[expected]++;
			else System.out.println("ex: " + expected + "   " + " real: " + real);
			//Assert.assertEquals(expected, real);
		}
		
		for(int i = 0; i < 10; i++) {
			System.out.println(i + " " + (correct[i]-2) + "/" + (total[i]-2));
		}
//		System.out.println(file.getAbsolutePath());
		
//		digitRecognition.recognize(record);
	}
	
	@Test
	public void printProbMatrix() throws Exception {
		DigitRecognition digitRecognition = new DigitRecognition();
		float[][] probabilityMatrix = digitRecognition.getProbabilityMatrix();
		
		for(int i = 0; i < probabilityMatrix.length; i++) {
			for(int j = 0; j < probabilityMatrix[i].length; j++) {
				System.out.printf("%.3f ", probabilityMatrix[i][j]);
			}
			System.out.println();
		}
	}
	
	@Test
	public void testProbMatrix() throws Exception {
		
		File dir = new File("data");	
		
		List<RecordItem> record8 = new Gson().fromJson(new FileReader(new File(dir, "85.txt")), new TypeToken<List<RecordItem>>(){}.getType());
		List<RecordItem> record0 = new Gson().fromJson(new FileReader(new File(dir, "05.txt")), new TypeToken<List<RecordItem>>(){}.getType());
		
		
		DigitRecognition digitRecognition = new DigitRecognition();
		float[][] probabilityMatrix = digitRecognition.getProbabilityMatrix();
		
		int out8 = digitRecognition.recognize(record8);
		int out0 = digitRecognition.recognize(record0);
		
		System.out.println(out8 + " " + out0);
		
		float prob = probabilityMatrix[out8][8] * probabilityMatrix[out0][0];
		System.out.println(prob);
	}
	
	public void testDrawMatrix() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		File dir = new File("data");
		
		File file = new File(dir, "41.txt");
		List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
		
		DigitRecognition digitRecognition = new DigitRecognition();
		digitRecognition.drawMatrix(record);
	}
	
	public static void testGetColums(String filename, int res) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		File dir = new File("data");
		
		File file = new File(dir, filename);
		List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
		
		DigitRecognition digitRecognition = new DigitRecognition();
		int cols[] = digitRecognition.getColumns(record, res);
//		System.out.println(Arrays.toString(cols));
		int power[] = new int[15];
		for(int i : cols) power[i]++;
		System.out.println(Arrays.toString(power));
	}
	
	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		File dir = new File("data");
		File[] files = dir.listFiles();
		for(File file : files) {
//			int expected = file.getName().charAt(0) - '0';
			System.out.println(file.getName());
			testGetColums(file.getName(), 150);
		}
		
		
		
	}
	
	
}
