package org.pesho.math;

public class RecordItem {
	private float x, y;
	private int action;
	
	public RecordItem(float x, float y, int action) {
		super();
		this.x = x;
		this.y = y;
		this.action = action;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
	
	
	
	
}
