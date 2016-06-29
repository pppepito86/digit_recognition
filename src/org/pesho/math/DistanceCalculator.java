package org.pesho.math;

import java.awt.geom.Point2D;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DistanceCalculator {
	
	public static void main(String[] args) throws Exception {
		List<RecordItem> current, original;
		current = new Gson().fromJson(new FileReader("data/44.txt"), new TypeToken<List<RecordItem>>(){}.getType());
		for (int i = 0; i <= 9; i++) {
			System.out.print(i + ": ");
			for (int j = 4; j < 8; j++) {
				original = new Gson().fromJson(new FileReader("data/"+i + j +".txt"), new TypeToken<List<RecordItem>>(){}.getType());
				double diff = DistanceCalculator.calculate(current, original);
				System.out.print((int)diff + ", ");
			}
			System.out.println();
		}
	}
	
	public static int calculate(List<RecordItem> current) throws Exception {
		double minDiff = Double.MAX_VALUE;
		int digit = 0;
		for (int i = 0; i <= 9; i++) {
			double d = 0;
			for (int j = 4; j < 8; j++) {
				List<RecordItem> original = new Gson().fromJson(new FileReader("data/"+i + j +".txt"), new TypeToken<List<RecordItem>>(){}.getType());
				double diff = DistanceCalculator.calculate(current, original);
				d += diff;
			}
			if (d < minDiff) {
				minDiff = d;
				digit = i;
			}
		}
		return digit;
	}
	
	public static double calculate(List<RecordItem> current, List<RecordItem> original) {
		scaleItem(current);
		scaleItem(original);
		double currentLength = calculateLength(current);
		double originalLength = calculateLength(original);
		double diff = 0;
		for (int i = 0; i < 100; i++) {
			Point2D.Double p1 = getPointsCoordinates(current, i, currentLength);
			Point2D.Double p2 = getPointsCoordinates(original, i, originalLength);
			double dist = p1.distance(p2);
			diff += dist*dist;
		}
		return diff;
	}
	
	public static Point2D.Double getPointsCoordinates(List<RecordItem> record, int pointNumber) {
		scaleItem(record);
		double recordLength = calculateLength(record);
		return getPointsCoordinates(record, pointNumber, recordLength);
	}


	private static Point2D.Double getPointsCoordinates(List<RecordItem> record, int pointNumber, double currentLength) {
		double distanceToPoint = currentLength * (pointNumber - 1) / 100; 
		double distance = 0;
		for (int i = 1; i < record.size(); i++) {
        	RecordItem i1 = record.get(i-1);
        	RecordItem i2 = record.get(i);
        	if (i2.getAction() == 0) continue;
        	double currentDistance = Point2D.distance(i1.getX(), i1.getY(), i2.getX(), i2.getY());
        	if (distance + currentDistance >= distanceToPoint) {
        		double partialDistance = distanceToPoint - distance;
        		double x = i1.getX() + (i2.getX() - i1.getX()) * partialDistance / currentDistance;
        		double y = i1.getY() + (i2.getY() - i1.getY()) * partialDistance / currentDistance;
        		Point2D.Double point = new Point2D.Double(x, y);
        		return point;
        	}
        	distance += currentDistance;
		}
		return null;
	}

	private static double calculateLength(List<RecordItem> record) {
		double distance = 0;
		for (int i = 1; i < record.size(); i++) {
        	RecordItem i1 = record.get(i-1);
        	RecordItem i2 = record.get(i);
        	if (i2.getAction() == 0) continue;
        	double currentDistance = Point2D.distance(i1.getX(), i1.getY(), i2.getX(), i2.getY());
        	distance += currentDistance;
		}
		return distance;
	}

	private static void scaleItem(List<RecordItem> record) {
		float minX = 10, minY = 10, maxX = 0f, maxY = 0f;
        for (int i = 0; i < record.size(); i++) {
        	RecordItem item = record.get(i);
        	if (item.getX() < minX) minX = item.getX();
        	if (item.getY() < minY) minY = item.getY();
        	if (item.getX() > maxX) maxX = item.getX();
        	if (item.getY() > maxY) maxY = item.getY();
        }
        
		for (int i = 0; i < record.size(); i++) {
        	RecordItem item = record.get(i);
        	float x = item.getX() - minX; x *= 1f/(maxX-minX);
        	float y = item.getY() - minY; y *= 1f/(maxY-minY);
        	item.setX(x);
        	item.setY(y);
        }
	}

}
