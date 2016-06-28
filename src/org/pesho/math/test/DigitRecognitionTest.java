package org.pesho.math.test;

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
	
	@Ignore
	@Test
	public void testAll() throws Exception {
		DigitRecognition digitRecognition = new DigitRecognition();
		
		File dir = new File("data");
		File[] files = dir.listFiles();
		for(File file : files) {
			int expected = file.getName().charAt(0) - '0';
			List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
			int real = digitRecognition.recognize(record);
			
			Assert.assertEquals(expected, real);
		}
		
//		System.out.println(file.getAbsolutePath());
		
//		digitRecognition.recognize(record);
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
