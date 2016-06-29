package org.pesho.math;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JFrame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LinearVisualiser extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int X = 1000, Y = 400;

	List<Double> data;

	@SuppressWarnings("unchecked")
	public LinearVisualiser(List<Double> data) throws Exception {
		this.data = data;

		setSize(X, Y + 30);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		drawDigit(g2, data);
	}

	private void drawDigit(Graphics2D g2, List<Double> record) {
		
		double minX = 0, minY = Y, maxX = record.size()-1, maxY = 0f;
		
		
        for (int i = 0; i < record.size(); i++) {
        	if (record.get(i) < minY) minY = record.get(i);
        	if (record.get(i) > maxY) maxY = record.get(i);
        }
        
		for (int i = 1; i < record.size(); i++) {
			if(Math.abs(record.get(i) - record.get(i-1)) < Math.PI/2) {
	        	double x1 = (i-1); x1 *= X/(maxX-minX);
	        	double y1 = record.get(i-1) - minY; y1 *= Y/(maxY-minY);
	        	double x2 = i; x2 *= X/(maxX-minX);
	        	double y2 = record.get(i) - minY; y2 *= Y/(maxY-minY);
	            Line2D line = new Line2D.Double(x1, y1+30, x2, y2+30);
	            g2.draw(line);
			}
        }
		
		Line2D line = new Line2D.Double(0, - minY * Y/(maxY-minY) + 30, X, - minY * Y/(maxY-minY) +30);
        g2.draw(line);
	}

	public static void plot(List<Double> data) {
		LinearVisualiser v = null;
		try {
			v = new LinearVisualiser(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		v.setVisible(true);
		v.setDefaultCloseOperation(2);
	}
}
