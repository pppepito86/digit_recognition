package org.pesho.math;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AngleComparisson {
	
	public List<Double> getAngles(List<RecordItem> record) {
		List<Double> angles = new ArrayList<Double>();
		
		for(int j = 1; j < record.size(); j++) {
			if(record.get(j-1).getAction() == 1) continue;
			
			double dx = record.get(j).getX() - record.get(j-1).getX();
			double dy = record.get(j).getY() - record.get(j-1).getY();
			
			double theta = Math.atan2(dy, dx);
			
			if(j!=1){
				double last = angles.get(angles.size()-1);
				if(Math.abs(last - theta) > Math.PI) {
					if(last < 0) theta -= 2*Math.PI;
					else theta += 2*Math.PI;
				}
			}
			angles.add(theta);
		}
		
		return angles;
	}
	
	public List<Double> smooth(List<Double> data) {
		List<Double> smooth = new ArrayList<>(data);
		
		int radius = 5;
		double threshold = Math.PI/2;
		
		int count;
		double sum;
		
		int n = data.size();
		
		
		for(int i = 0; i < n; i++) {
			count = 1;
			sum = data.get(i);
			
			for(int j = i+1; j <= i+radius; j++) {
				if(j < n && Math.abs(data.get(j) - data.get(j-1)) < threshold ) {
					count++;
					sum += data.get(j);
				} else {
					break;
				}
			}
			
			for(int j = i-1; j >= i-radius; j--) {
				if(j >= 0 && Math.abs(data.get(j) - data.get(j+1)) < threshold ) {
					count++;
					sum += data.get(j);
				} else {
					break;
				}
			}
			
			smooth.set(i, sum / count);
		}
		
		return smooth;
	}
	
	public void analyze (List<Double> data) {
		int MIN_LEN = 5; //data.size() / 6;
		final double MAX_LINE_OSCILLATION = Math.PI / 6;
		
		for(int i = 0; i < data.size(); i++) {
			double startAngle = data.get(i);
			double total = startAngle;
			
			for(int j = i+1; j < data.size(); j++) {				
				if(Math.abs(startAngle - data.get(j)) > MAX_LINE_OSCILLATION || (Math.abs(data.get(j) - data.get(j-1)) > Math.PI/2)) { 
					if( j - i > MIN_LEN) {
						System.out.println("Line with angle: " + (total/(j-i))*180/Math.PI + " from " + i + " to " + j);
						i = j;
					}
					break;
				}
				
				total += data.get(j);
			}
		}
		
		
		for(int i = 0; i < data.size(); i++) {
			double startAngle = data.get(i);
			double total = startAngle;
			
			for(int j = i+1; j < data.size(); j++) {
				if(!(data.get(j) >= data.get(j-1)) || (Math.abs(data.get(j) - data.get(j-1)) > Math.PI/2)) { 
					if( j - i > MIN_LEN && Math.abs(data.get(j-1) - data.get(i)) > MAX_LINE_OSCILLATION) {
						System.out.println("Arc with angle: " +(data.get(j-1) - data.get(i))*180/Math.PI + " from " + i + " to " + j);
						i = j;
					}
					break;
				}
			}
			
			for(int j = i+1; j < data.size(); j++) {
				if(!(data.get(j) <= data.get(j-1)) || (Math.abs(data.get(j) - data.get(j-1)) > Math.PI/2)) { 
					if( j - i > MIN_LEN && Math.abs(data.get(j-1) - data.get(i)) > MAX_LINE_OSCILLATION) {
						System.out.println("Arc with angle: " + (data.get(j-1) - data.get(i))*180/Math.PI + " from " + i + " to " + j);
						i = j;
					}
					break;
				}
			}
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		File dir = new File("data");
		
		File file = new File(dir, "34.txt");
		
		Visualiser v = new Visualiser(new File[]{file});
		v.setVisible(true);
		v.setDefaultCloseOperation(2);
		
//		File[] files = dir.listFiles();
//		for(File file : files) {
			System.out.print(file.getName() + ", ");
			List<RecordItem> record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
			AngleComparisson angleComparisson = new AngleComparisson();
			List<Double> angles = angleComparisson.getAngles(record);
			System.out.println(angles);
//		}
		
			List<Double> smooth = angleComparisson.smooth(angles);
			
		LinearVisualiser.plot(angles);
		LinearVisualiser.plot(smooth);
		
		angleComparisson.analyze(smooth);
		
		
	}
	
}
