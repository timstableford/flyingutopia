package flyingutopia.engine.interactions;

import flyingutopia.engine.Sprite;
import flyingutopia.engine.world.Tile;

public interface WorldAction {
	//Return the hard-coded name in the class
	public String getName();
	//Return a clone
	public WorldAction getClone();
	//Parse your attribute string
	public void parseAttributes(String[] attributes);
	//Either collision or interact
	public void setInteraction(String type);
	//On a sprite collision with this tile
	public void onCollision(Tile parent, Sprite source);
	//On separation after a collision
	public void onSeperate(Tile parent, Sprite source);
	//On interaction by a sprite
	public void onInteract(Tile parent, Sprite source);
}
