package flyingutopia.engine.interactions;

import org.mangosdk.spi.ProviderFor;

import flyingutopia.engine.resources.ImageResource;
import flyingutopia.engine.resources.ImageResources;
import flyingutopia.engine.sprite.Sprite;
import flyingutopia.engine.timer.Timer;
import flyingutopia.engine.timer.TimerManager;
import flyingutopia.engine.timer.Timers;
import flyingutopia.engine.world.Tile;

@ProviderFor(WorldAction.class)
public class DoorAction implements WorldAction{
	protected static final String name = "door";
	protected ImageResource openRes;
	protected ImageResource closedRes;
	protected boolean isOpen;
	protected InteractionTypes interactionType;
	protected long initialOpenTime, openTime;
	protected long lastInteraction;
	protected Tile tile;
	public DoorAction() {
		isOpen = false;
		interactionType = InteractionTypes.NONE;
		initialOpenTime = 0;
	}
	@Override
	public String getName() {
		return name;
	}
	
	public boolean checkState(Tile parent) {
		if(parent.getResource() == openRes) {
			return true;
		}
		return false;
	}
	
	public void setDoorState(Tile parent, boolean open) {
		if(checkState(parent) == this.isOpen) {
			this.tile = parent;
			this.isOpen = open;
			parent.setForegroundSolid(!isOpen);
			if(isOpen) {
				parent.setResource(openRes);
				openRes.setCurrentFrame(0);
				parent.updateCollisionMap();
				lastInteraction = 0;
			} else {
				parent.setResource(closedRes);
				closedRes.setCurrentFrame(closedRes.getImage().length - 1);
				parent.updateCollisionMap();
				closedRes.setCurrentFrame(0);
			}
		}
	}

	@Override
	public WorldAction newInstance() {
		DoorAction d = new DoorAction();
		
		return d;
	}

	@Override
	public void parseAttributes(String[] attributes) {
		tile = null;
		openTime = initialOpenTime = Long.parseLong(attributes[0]);
		if("open".equals(attributes[1])) {
			isOpen = true;
		} else {
			isOpen = false;
		}
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
			setDoorState(parent, true);
		}
	}

	@Override
	public void onSeperate(Tile parent, Sprite source) {
	}

	@Override
	public void onInteract(Tile parent, Sprite source) {
		if(this.interactionType == InteractionTypes.INTERACT) {
			this.setDoorState(parent, !isOpen);
		}
	}
	@Override
	public void setupTimers(Tile parent) {
		if(initialOpenTime > 0) {
			lastInteraction = 0;
			TimerManager.addTimer(Timers.WORLD, new Timer() {
				@Override
				public void onTimer(long millis) {
					if(isOpen && tile != null && lastInteraction < openTime) {
						lastInteraction += millis;
						if(lastInteraction >= openTime) {
							setDoorState(tile, false);
							lastInteraction = 0;
							openTime = initialOpenTime;
						}
					}
				}
			});
		}
	}

}
