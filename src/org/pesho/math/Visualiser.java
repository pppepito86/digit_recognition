package org.pesho.math;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JFrame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Visualiser extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	List<RecordItem> record;

	public Visualiser(File file) throws Exception {
		record = new Gson().fromJson(new FileReader(file), new TypeToken<List<RecordItem>>(){}.getType());
        setSize(600,900);
	}

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 1; i < record.size(); i++) {
        	RecordItem i1 = record.get(i-1);
        	RecordItem i2 = record.get(i);
            Line2D line = new Line2D.Float(i1.getX()* 600, i1.getY()*900, i2.getX()*600, i2.getY()*900);
            g2.draw(line);
        }
    }
	

	public static void main(String[] args) throws Exception {
		Visualiser v = new Visualiser(new File("data/00.txt"));
		v.setVisible(true);
		v.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

}
