package org.pesho.math;

import java.util.List;

public class DistanceCalculator {
	
	public static double calculate(List<RecordItem> current, List<RecordItem> original) {
		scaleItem(current);
		scaleItem(original);
		
		return 0;
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
