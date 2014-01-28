package flyingutopia.engine;

import flyingutopia.engine.world.Tile;

public interface WorldAction {
	//Return the hard-coded name in the class
	public String getName();
	//Return a clone
	public WorldAction getClone();
	//Parse your attribute string
	public void parseAttributes(String attributes);
	//Either collision or interact
	public void setInteraction(String type);
	//On a sprite collision with this tile
	public void onCollision(Tile parent);
	//On separation after a collision
	public void onSeperate(Tile parent);
	//On interaction by a sprite
	public void onInteract(Tile parent);
}
