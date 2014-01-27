package flyingutopia.engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

public class Sprite implements Timer{
	protected double DEFAULT_SPEED = 60;
	protected Vector2D velocity;
	protected double x, y;
	protected int direction;
	protected ImageResource image;
	protected HashMap<String, ImageResource> resources;
	protected double speed;
	public Sprite(double x, double y) {
		this.x = x;
		this.y = y;
		velocity = new Vector2D();
		this.direction = 0;
		this.speed = DEFAULT_SPEED;
		resources = new HashMap<String, ImageResource>();
	}
	public void setResource(ImageResource res) {
		this.image = res;
	}
	public void onTimer(long millis) {
		double dt = (double)millis/1000;
		this.x += velocity.getX() * dt;
		this.y += velocity.getY() * dt;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public Vector2D getVelocity() {
		return this.velocity;
	}
	public double getSpeed() {
		return this.speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public void render(Graphics g) {	
		Graphics2D g2 = (Graphics2D)g;
		ImageResource r = this.image;
		if(resources.containsKey(this.velocity.getDirection().toString())) {
			r = resources.get(this.velocity.getDirection().toString());
		}
		if(r != null) {
			g2.drawImage(r.getImage().getImage(), (int)x, (int)y, null);
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
