package flyingutopia.engine.sprite;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import flyingutopia.engine.Focus;
import flyingutopia.engine.ImageResource;
import flyingutopia.engine.timer.Timer;
import flyingutopia.engine.vector.Vector2D;
import flyingutopia.engine.world.Tile;

public abstract class SpriteCommon implements Timer, Focus, Sprite{
	protected double DEFAULT_SPEED = 70;
	protected Vector2D velocity;
	protected double x, y;
	protected int direction;
	protected ImageResource image;
	protected HashMap<String, ImageResource> resources;
	protected double speed;
	protected String name;
	public SpriteCommon(String name, double x, double y) {
		this.x = x;
		this.y = y;
		velocity = new Vector2D();
		this.direction = 0;
		this.speed = DEFAULT_SPEED;
		this.name = name;
		resources = new HashMap<String, ImageResource>();
	}
	
	public SpriteCommon() {
		this(null, 0, 0);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setStartResource(ImageResource res) {
		this.image = res;
	}
	
	@Override
	public void onTimer(long millis) {
		double dt = (double)millis/1000;
		this.x += velocity.getX() * dt;
		this.y += velocity.getY() * dt;
	}
	
	@Override
	public void interact(Tile t) {
		t.onInteract(this);
	}
	
	@Override
	public void render(Graphics g) {	
		Graphics2D g2 = (Graphics2D)g;
		ImageResource r = this.image;
		if(resources.containsKey(this.velocity.getDirection().toString())) {
			r = resources.get(this.velocity.getDirection().toString());
			this.image = r;
		}
		if(r != null) {
			g2.drawImage(r.getImage()[0].getImage(), (int)x - r.getImage()[0].getImage().getWidth()/2,
					(int)y - r.getImage()[0].getImage().getHeight()/2, null);
		}
	}
	
	@Override
	public int getWidth() {
		if(this.image != null) {
			return this.image.getImage()[0].getImage().getWidth();
		}
		return 0;
	}
	
	@Override
	public int getHeight() {
		if(this.image != null) {
			return this.image.getImage()[0].getImage().getHeight();
		}
		return 0;
	}
	
	@Override
	public double getX() {
		return x;
	}
	
	@Override
	public void setX(double x) {
		this.x = x;
	}
	
	@Override
	public double getY() {
		return y;
	}
	
	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public Vector2D getVelocity() {
		return this.velocity;
	}
	
	@Override
	public double getSpeed() {
		return this.speed;
	}
	
	@Override
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
}
