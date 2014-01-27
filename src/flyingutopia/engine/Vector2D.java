package flyingutopia.engine;

public class Vector2D {
	protected double x,y;
	protected Direction direction;
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
		this.direction = Direction.RIGHT;
	}
	public Vector2D() {
		this(0,0);
	}
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getSpeed() {
		if(this.x != 0 || this.y != 0) {
			return Math.sqrt(this.x * this.x + this.y * this.y);
		} else {
			return 0;
		}
	}
	public double getXSpeed() {
		return Math.abs(this.x);
	}
	public double getYSpeed() {
		return Math.abs(this.y);
	}
	public Direction getDirection() {
		if(this.x > 0) {
			direction = Direction.RIGHT;
		}else if(this.x < 0) {
			direction = Direction.LEFT;
		}
		if(this.y > 0) {
			direction = Direction.DOWN;
		}else if(this.y < 0) {
			direction = Direction.UP;
		}
		return direction;
	}
}
