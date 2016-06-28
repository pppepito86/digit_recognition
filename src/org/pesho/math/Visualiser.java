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

public class Visualiser extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final int X = 400, Y = 600;
	
	List<RecordItem>[] record;

	@SuppressWarnings("unchecked")
	public Visualiser(File... files) throws Exception {
		record = new List[files.length];
		for (int i = 0; i < files.length; i++) {
			record[i] = new Gson().fromJson(new FileReader(files[i]), new TypeToken<List<RecordItem>>(){}.getType());
		}
        setSize(X,Y+20);
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < record.length; i++) {
			drawDigit(g2, record[i]);
		}
    }

	private void drawDigit(Graphics2D g2, List<RecordItem> record) {
		float minX = X, minY = Y, maxX = 0f, maxY = 0f;
        for (int i = 0; i < record.size(); i++) {
        	RecordItem item = record.get(i);
        	if (item.getX() < minX) minX = item.getX();
        	if (item.getY() < minY) minY = item.getY();
        	if (item.getX() > maxX) maxX = item.getX();
        	if (item.getY() > maxY) maxY = item.getY();
        }
		for (int i = 1; i < record.size(); i++) {
        	RecordItem i1 = record.get(i-1);
        	RecordItem i2 = record.get(i);
        	if (i2.getAction() == 0) continue;
        	float x1 = i1.getX() - minX; x1 *= X/(maxX-minX);
        	float y1 = i1.getY() - minY; y1 *= Y/(maxY-minY);
        	float x2 = i2.getX() - minX; x2 *= X/(maxX-minX);
        	float y2 = i2.getY() - minY; y2 *= Y/(maxY-minY);
            Line2D line = new Line2D.Float(x1, y1+20, x2, y2+20);
            g2.draw(line);

        }
        for (int i = 0; i < record.size(); i++) {
        	RecordItem item = record.get(i);
        	float x = item.getX() - minX; x *= X/(maxX-minX);
        	float y = item.getY() - minY; y *= Y/(maxY-minY);
            Ellipse2D ellipse = new Ellipse2D.Float((float)(x-2.5), (float)(y-2.5)+20f, 5f, 5f);
            g2.draw(ellipse);
        }
	}
	

	public static void main(String[] args) throws Exception {
		String digit = "5";
		File[] files = new File[3];
		for (int i = 0; i < files.length; i++) {
			files[i] = new File("data/"+digit+(10+i)+".txt");
		}
		Visualiser v = new Visualiser(files);
		v.setVisible(true);
		v.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

}
