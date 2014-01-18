package flyingutopia.engine.world;

import flyingutopia.engine.Resource;

public class Tile {
	private Resource resource;
	private Resource background;
	private int x,y;
	private boolean solid;
	private String action;
	private String attribute;
	public Tile(int x, int y) {
		this.resource = null;
		this.background = null;
		this.solid = false;
		this.attribute = "";
		this.action = "";
		this.x = x;
		this.y = y;
		
	}
	public Resource getBackground() {
		return background;
	}
	public void setBackground(Resource bg) {
		this.background = bg;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isSolid() {
		return solid;
	}
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	public String getAttribute() {
		return attribute;
	}
	public String getAction() {
		return this.action;
	}
	public void setAction(String action, String attribute) {
		this.action = action;
		this.attribute = attribute;
	}
	
}
