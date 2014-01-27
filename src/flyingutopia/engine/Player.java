package flyingutopia.engine;

import flyingutopia.engine.world.Level;

public class Player extends Sprite implements WorldCollidable{

	public Player(double x, double y) {
		super(x, y);
		this.setStartResource(ImageResources.getInstance().getResource("boy_front"));
		this.resources.put("left", ImageResources.getInstance().getResource("boy_left"));
		this.resources.put("down", ImageResources.getInstance().getResource("boy_front"));
		this.resources.put("up", ImageResources.getInstance().getResource("boy_back"));
		this.resources.put("right", ImageResources.getInstance().getResource("boy_right"));
	}

	@Override
	public boolean checkForCollisions(Level level) {
		boolean colliding = false;
		if(level.isColliding(getX() - getWidth()/2, getY())) {
			if(this.velocity.getX() < 0) {
				this.velocity.setX(0);
			}
			colliding = true;
		}
		if(level.isColliding(getX() + getWidth()/2, getY())) {
			if(this.velocity.getX() > 0) {
				this.velocity.setX(0);
			}
			colliding = true;
		}
		if(level.isColliding(getX(), getY() + getHeight()/2)) {
			if(this.velocity.getY() > 0) {
				this.velocity.setY(0);
			}
			colliding = true;
		}
		if(level.isColliding(getX(), getY() - getHeight()/6)) {
			if(this.velocity.getY() < 0) {
				this.velocity.setY(0);
			}
			colliding = true;
		}
		return colliding;
	}

}
