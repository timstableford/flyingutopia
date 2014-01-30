package flyingutopia.engine.interactions;

import org.mangosdk.spi.ProviderFor;

import flyingutopia.engine.ImageResource;
import flyingutopia.engine.ImageResources;
import flyingutopia.engine.Sprite;
import flyingutopia.engine.world.Tile;

@ProviderFor(WorldAction.class)
public class DoorAction implements WorldAction{
	protected static final String name = "door";
	protected ImageResource openRes;
	protected ImageResource closedRes;
	protected boolean isOpen;
	protected InteractionTypes interactionType;
	public DoorAction() {
		isOpen = false;
		interactionType = InteractionTypes.NONE;
		System.out.println(name+" action instantiated");
	}
	@Override
	public String getName() {
		return name;
	}
	
	public void setDoorState(Tile parent, boolean open) {
		this.isOpen = open;
		parent.setForegroundSolid(!isOpen);
		if(isOpen) {
			parent.setResource(openRes);
			openRes.setCurrentFrame(0);
			parent.updateCollisionMap();
		} else {
			parent.setResource(closedRes);
			closedRes.setCurrentFrame(closedRes.getImage().length - 1);
			parent.updateCollisionMap();
			closedRes.setCurrentFrame(0);
		}
	}
	
	@Override
	public void onTimer(long dt) {
		
	}

	@Override
	public WorldAction getClone() {
		DoorAction d = new DoorAction();
		
		return d;
	}

	@Override
	public void parseAttributes(String[] attributes) {
		openRes = ImageResources.getInstance().getResource(attributes[2]);
		closedRes = ImageResources.getInstance().getResource(attributes[3]);
	}

	@Override
	public void setInteraction(InteractionTypes type) {
		this.interactionType = type;	
	}

	@Override
	public void onCollision(Tile parent, Sprite source) {
		if(this.interactionType == InteractionTypes.COLLIDE) {
			System.out.println(name+" collision");
		}
	}

	@Override
	public void onSeperate(Tile parent, Sprite source) {
		if(this.interactionType == InteractionTypes.COLLIDE) {
			System.out.println(name+" seperate");
		}
	}

	@Override
	public void onInteract(Tile parent, Sprite source) {
		if(this.interactionType == InteractionTypes.INTERACT) {
			this.setDoorState(parent, !isOpen);
		}
	}

}
