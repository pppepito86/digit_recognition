package org.pesho.math.test;

import java.io.FileReader;
import java.util.List;

import org.junit.Test;
import org.pesho.math.DistanceCalculator;
import org.pesho.math.RecordItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DistanceCalculatorTest {

	@Test
	public void test() throws Exception {
		for (int i = 0; i <= 9; i++) {
			for (int j = 4; j <= 7; j++) {
				List<RecordItem> current = new Gson().fromJson(new FileReader("data/"+i+j+".txt"), new TypeToken<List<RecordItem>>(){}.getType());
				int digit = DistanceCalculator.calculate(current);
				System.out.println("real: " + i + ", curr: " + digit);
			}
		}
	}
	
}
