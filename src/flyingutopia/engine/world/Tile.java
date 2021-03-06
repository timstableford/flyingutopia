package flyingutopia.engine.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import flyingutopia.engine.interactions.ActionParser;
import flyingutopia.engine.interactions.WorldAction;
import flyingutopia.engine.resources.ImageResource;
import flyingutopia.engine.resources.ImageResources;
import flyingutopia.engine.sprite.Sprite;
import flyingutopia.engine.sprite.SpriteInteractable;
import static argo.jdom.JsonNodeBuilders.*;

public class Tile implements SpriteInteractable{
	protected ImageResource resource;
	protected ImageResource background;
	protected int x,y;
	protected boolean backgroundSolid, foregroundSolid;
	protected String action;
	protected String attribute;
	protected List<WorldAction> actions;
	protected boolean collisionMap[][];
	protected boolean renderRequired;
	public Tile(int x, int y) {
		this.resource = null;
		this.background = null;
		this.foregroundSolid = false;
		this.backgroundSolid = false;
		this.attribute = "";
		this.action = "";
		this.x = x;
		this.y = y;
		this.actions = ActionParser.parseActions(action, attribute);
		this.renderRequired = true;
	}

	public Tile(JsonNode node) {
		this(Integer.parseInt(node.getNumberValue("x")), Integer.parseInt(node.getNumberValue("y")));
		resource = ImageResources.getInstance().getResource(node.getStringValue("foreground"));
		background = ImageResources.getInstance().getResource(node.getStringValue("background"));
		backgroundSolid = Boolean.parseBoolean(node.getStringValue("backgroundsolid"));
		foregroundSolid = Boolean.parseBoolean(node.getStringValue("foregroundsolid"));
		attribute = node.getStringValue("attribute");
		action = node.getStringValue("action");
		this.actions = ActionParser.parseActions(action, attribute);
	}

	public Tile getCopy() {
		Tile t = new Tile(x, y);
		t.resource = resource;
		t.background = background;
		t.foregroundSolid = foregroundSolid;
		t.backgroundSolid = backgroundSolid;
		t.attribute = attribute;
		t.action = action;
		t.renderRequired = true;
		return t;
	}

	public ImageResource getBackground() {
		return background;
	}
	public void setBackground(ImageResource bg) {
		this.background = bg;
		this.renderRequired = true;
	}
	public ImageResource getResource() {
		return resource;
	}
	public void setResource(ImageResource resource) {
		this.resource = resource;
		this.renderRequired = true;
	}

	public boolean [][] getCollisionMap(int COLLISION_RESOLUTION) {
		if(collisionMap == null) {
			collisionMap = new boolean[COLLISION_RESOLUTION][COLLISION_RESOLUTION];
			if(this.isBackgroundSolid()) {
				for(int ax=0; ax<COLLISION_RESOLUTION; ax++) {
					for(int ay=0; ay<COLLISION_RESOLUTION; ay++) {
						collisionMap[ax][ay] = true;
					}
				}
			} else if(this.getResource() != null && this.isForegroundSolid()){
				long[][] pixelData = new long[ImageResources.TILE_SIZE][ImageResources.TILE_SIZE];
				BufferedImage b = this.getResource().getImage()[this.getResource().getCurrentFrame()].getImage();
				for(int xx=0; xx<ImageResources.TILE_SIZE; xx++) {
					for(int yy=0; yy<ImageResources.TILE_SIZE; yy++) {
						pixelData[xx][yy] = (b.getRGB(xx, yy) >> 24) & 0xFF;
					}
				}
				for(int ax=0; ax<COLLISION_RESOLUTION; ax++) {
					for(int ay=0; ay<COLLISION_RESOLUTION; ay++) {
						long average = 0;
						for(int bx=0; bx < ImageResources.TILE_SIZE/COLLISION_RESOLUTION; bx++) {
							for(int by=0; by < ImageResources.TILE_SIZE/COLLISION_RESOLUTION; by++) {
								average += pixelData[bx + ax * ImageResources.TILE_SIZE/COLLISION_RESOLUTION][by + ay * ImageResources.TILE_SIZE/COLLISION_RESOLUTION];
							}
						}
						average = average/((ImageResources.TILE_SIZE/COLLISION_RESOLUTION)*(ImageResources.TILE_SIZE/COLLISION_RESOLUTION));
						collisionMap[ax][ay] = (average > 10) || (average < -10);
					}
				}
			}
		}
		return collisionMap;
	}
	
	public void updateCollisionMap() {
		collisionMap = null;
		this.getCollisionMap(Level.COLLISION_RESOLUTION);
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
		.withField("backgroundsolid", aStringBuilder(Boolean.toString(backgroundSolid)))
		.withField("foregroundsolid", aStringBuilder(Boolean.toString(foregroundSolid)))
		.withField("attribute", aStringBuilder(attribute))
		.withField("action", aStringBuilder(action))
		.withField("x", aNumberBuilder(Integer.toString(x)))
		.withField("y", aNumberBuilder(Integer.toString(y)));
		return builder;
	}
	
	public boolean renderRequired() {
		boolean render = renderRequired;
		if(renderRequired) {
			renderRequired = false;
		}
		if(this.getBackground() != null && this.getBackground().animate()) {
			render = true;
		}
		if(this.getResource() != null && this.getResource().animate()) {
			render = true;
		}
		return render;
	}
	
	public void render(Graphics g) {
		if(this.getBackground() != null) {
			g.drawImage(this.getBackground().getImage()[this.getBackground().getCurrentFrame()].getImage(),
					(int)(x*ImageResources.TILE_SIZE),
					(int)(y*ImageResources.TILE_SIZE),
					(int)(ImageResources.TILE_SIZE),
					(int)(ImageResources.TILE_SIZE), null);
		}
		if(this.getResource() != null) {
			g.drawImage(this.getResource().getImage()[this.getResource().getCurrentFrame()].getImage(),
					(int)(x*ImageResources.TILE_SIZE),
					(int)(y*ImageResources.TILE_SIZE),
					(int)(ImageResources.TILE_SIZE),
					(int)(ImageResources.TILE_SIZE), null);
		}
	}
	
	public void setupActionTimers() {
		for(WorldAction w: actions) {
			w.setupTimers(this);
		}
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
	public boolean isForegroundSolid() {
		return foregroundSolid;
	}
	public void setForegroundSolid(boolean solid) {
		this.foregroundSolid = solid;
	}
	public boolean isBackgroundSolid() {
		return backgroundSolid;
	}
	public void setBackgroundSolid(boolean solid) {
		this.backgroundSolid = solid;
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

	@Override
	public void onCollision(Sprite source) {
		for(WorldAction w: actions) {
			w.onCollision(this, source);
		}
	}

	@Override
	public void onSeperate(Sprite source) {
		for(WorldAction w: actions) {
			w.onSeperate(this, source);
		}
	}

	@Override
	public void onInteract(Sprite source) {
		for(WorldAction w: actions) {
			w.onInteract(this, source);
		}
	}

}
