package flyingutopia.engine.interactions;

import org.mangosdk.spi.ProviderFor;

import flyingutopia.engine.Sprite;
import flyingutopia.engine.world.Tile;

@ProviderFor(WorldAction.class)
public class DoorAction implements WorldAction{
	private static final String name = "door";
	public DoorAction() {
		System.out.println(name+" action instantiated");
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public WorldAction getClone() {
		DoorAction d = new DoorAction();
		
		return d;
	}

	@Override
	public void parseAttributes(String[] attributes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInteraction(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollision(Tile parent, Sprite source) {
		System.out.println(name+" collision");	
	}

	@Override
	public void onSeperate(Tile parent, Sprite source) {
		System.out.println(name+" seperate");
		
	}

	@Override
	public void onInteract(Tile parent, Sprite source) {
		System.out.println(name+" interact");
	}

}
