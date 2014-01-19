package flyingutopia.engine;

import java.awt.Graphics;

public class Sprite {
	protected double x, y;
	protected ImageResource image;
	public Sprite(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setResource(ImageResource res) {
		this.image = res;
	}
	public void render(Graphics g) {
		if(image != null) {
			g.drawImage(this.image.getImage().getImage(), (int)x, (int)y, null);
		}
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
