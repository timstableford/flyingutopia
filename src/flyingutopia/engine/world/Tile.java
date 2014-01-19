package flyingutopia.engine.world;

import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import flyingutopia.engine.ImageResource;
import flyingutopia.engine.ImageResources;
import static argo.jdom.JsonNodeBuilders.*;

public class Tile {
	private ImageResource resource;
	private ImageResource background;
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
	
	public Tile(JsonNode node) {
		resource = ImageResources.getInstance().getResource(node.getStringValue("foreground"));
		background = ImageResources.getInstance().getResource(node.getStringValue("background"));
		solid = Boolean.parseBoolean(node.getStringValue("solid"));
		attribute = node.getStringValue("attribute");
		action = node.getStringValue("action");
		x = Integer.parseInt(node.getNumberValue("x"));
		y = Integer.parseInt(node.getNumberValue("y"));
	}
	
	public Tile getCopy() {
		Tile t = new Tile(x, y);
		t.resource = resource;
		t.background = background;
		t.solid = solid;
		t.attribute = attribute;
		t.action = action;
		return t;
	}
	
	public ImageResource getBackground() {
		return background;
	}
	public void setBackground(ImageResource bg) {
		this.background = bg;
	}
	public ImageResource getResource() {
		return resource;
	}
	public void setResource(ImageResource resource) {
		this.resource = resource;
	}

	public JsonObjectNodeBuilder getJson() {
		String resourceName = "";
		String backgroundName = "";
		if(resource != null) {
			resourceName = resource.getName();
		}
		if(background != null) {
			backgroundName = background.getName();
		}
		JsonObjectNodeBuilder builder = anObjectBuilder();
		builder.withField("foreground", aStringBuilder(resourceName))
			.withField("background", aStringBuilder(backgroundName))
			.withField("solid", aStringBuilder(Boolean.toString(solid)))
			.withField("attribute", aStringBuilder(attribute))
			.withField("action", aStringBuilder(action))
			.withField("x", aNumberBuilder(Integer.toString(x)))
			.withField("y", aNumberBuilder(Integer.toString(y)));
		return builder;
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
