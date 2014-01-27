package flyingutopia.engine;

public class Player extends Sprite {

	public Player(double x, double y) {
		super(x, y);
		this.setStartResource(ImageResources.getInstance().getResource("boy_front"));
		this.resources.put("left", ImageResources.getInstance().getResource("boy_left"));
		this.resources.put("down", ImageResources.getInstance().getResource("boy_front"));
		this.resources.put("up", ImageResources.getInstance().getResource("boy_back"));
		this.resources.put("right", ImageResources.getInstance().getResource("boy_right"));
	}

}
