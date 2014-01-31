package flyingutopia.engine.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import flyingutopia.engine.Engine;
import flyingutopia.engine.Focus;
import flyingutopia.engine.ImageResources;
import flyingutopia.engine.WorldCollidable;
import flyingutopia.engine.sprite.Sprite;
import flyingutopia.engine.sprite.SpriteCommon;
import flyingutopia.engine.sprite.SpriteManager;
import flyingutopia.engine.timer.TimerManager;
import flyingutopia.engine.timer.Timers;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import static argo.jdom.JsonNodeBuilders.*;

public class Level{
	public static final int COLLISION_RESOLUTION = 2;
	private Tile tiles[][];
	private ArrayList<Sprite> sprites;
	private int width;
	private int height;
	private String name;
	private BufferedImage buffer;
	private Focus focus;
	public Level(String name, int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		sprites = new ArrayList<Sprite>();
	}
	
	public Focus getFocus() {
		return focus;
	}
	
	public void setFocus(Focus focus) {
		this.focus = focus;
	}
	
	public void addSprite(SpriteCommon s) {
		this.sprites.add(s);
	}
	
	public void removeSprite(Sprite s) {
		this.sprites.remove(s);
	}
	
	public void setup(Engine engine) {
		TimerManager.reset(Timers.WORLD);
		for(int x=0; x < this.width; x++) {
			for(int y=0; y < this.height; y++) {
				Tile t = tiles[x][y];
				if(t != null) {
					t.setupActionTimers();
				}
			}
		}
		for(Sprite s: sprites) {
			s.setup();
			if(s instanceof KeyListener) {
				engine.addKeyListener((KeyListener)s);
			}
			if(s instanceof WorldCollidable) {
				engine.addCollidable((WorldCollidable)s);
			}
		}
	}
	
	private int checkInt(int max, int value) {
		if(value >= max) {
			value = max;
		}else if(value < 0) {
			value = 0;
		}
		return value;
	}
	
	public void render(Graphics g, int startx, int endx, int starty, int endy) {
		boolean force = false;
		if(buffer == null) {
			buffer = new BufferedImage(this.getWidth() * ImageResources.TILE_SIZE,
					this.getHeight() * ImageResources.TILE_SIZE,
					BufferedImage.TYPE_4BYTE_ABGR);
			force = true;
		}
		Graphics2D bufferG = buffer.createGraphics();
		
		endx = checkInt(this.getWidth(), endx);
		startx = checkInt(endx, startx);
		endy = checkInt(this.getHeight(), endy);
		starty = checkInt(endy, starty);

		if(force) {
			bufferG.setColor(Color.black);
			bufferG.fillRect((int)(startx * ImageResources.TILE_SIZE), (int)(starty * ImageResources.TILE_SIZE), 
					(int)(endx * ImageResources.TILE_SIZE), (int)(endy * ImageResources.TILE_SIZE));
		}
		for(int x=startx; x<endx && x<this.width; x++) {
			for(int y=starty; y<endy && y<this.height; y++) {
				Tile t = tiles[x][y];
				if(t != null) {
					if(t.renderRequired() || force) {
						t.render(bufferG);
					}
				}
			}
		}
		
		g.drawImage(buffer,
				(int)(startx * ImageResources.TILE_SIZE),
				(int)(starty * ImageResources.TILE_SIZE), 
				(int)(endx * ImageResources.TILE_SIZE),
				(int)(endy * ImageResources.TILE_SIZE),
				(int)(startx * ImageResources.TILE_SIZE),
				(int)(starty * ImageResources.TILE_SIZE), 
				(int)(endx * ImageResources.TILE_SIZE),
				(int)(endy * ImageResources.TILE_SIZE),
				null);
		
		for(Sprite s: sprites) {
			s.render(g);
		}
	}
	
	public void generateCollisionMap() {
		for(int x=0; x<this.width; x++) {
			for(int y=0; y<this.height; y++) {
				Tile t = tiles[x][y];
				if(t != null) {
					//generate initial collision map
					t.getCollisionMap(COLLISION_RESOLUTION);
				}
			}
		}
	}
	
	public boolean isColliding(double cx, double cy) {
		int x = (int) (cx/(ImageResources.TILE_SIZE / COLLISION_RESOLUTION));
		int y = (int) (cy/(ImageResources.TILE_SIZE / COLLISION_RESOLUTION));
		
		Tile t = tiles[(int) (cx / ImageResources.TILE_SIZE)][(int) (cy / ImageResources.TILE_SIZE)];
		if(t != null) {
			boolean tileMap[][] = t.getCollisionMap(COLLISION_RESOLUTION);
			if(tileMap != null) {
				x = x - t.getX() * COLLISION_RESOLUTION;
				y = y - t.getY() * COLLISION_RESOLUTION;
				return tileMap[x][y];
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		this.render(g, 0, this.width - 1, 0, this.height - 1);
	}
	
	public Level(JsonNode rootNode) {
		width = Integer.parseInt(rootNode.getNumberValue("width"));
		height = Integer.parseInt(rootNode.getNumberValue("height"));
		tiles = new Tile[width][height];
		name = rootNode.getStringValue("name");
		for(JsonNode n: rootNode.getArrayNode("tiles")) {
			Tile t = new Tile(n);
			tiles[t.getX()][t.getY()] = t;
		}
		sprites = new ArrayList<Sprite>();
		for(JsonNode n: rootNode.getArrayNode("sprites")) {
			String constructor = n.getText();
			Sprite s = SpriteManager.getSprite(constructor);
			this.sprites.add(s);
		}
		String f = rootNode.getStringValue("focus");
		if(f.length() > 0) {
			for(Sprite s: sprites) {
				if(s.getName().equals(f) && (s instanceof Focus)) {
					this.setFocus((Focus)s);
				}
			}
		}
	}

	public JsonObjectNodeBuilder getJson() {
		JsonArrayNodeBuilder arr = anArrayBuilder();
		for(int x=0; x<this.width; x++) {
			for(int y=0; y<this.height; y++) {
				Tile t = tiles[x][y];
				if(t != null) {
					arr.withElement(t.getJson());
				}
			}
		}
		JsonArrayNodeBuilder arr2 = anArrayBuilder();
		for(Sprite s: sprites) {
			arr2.withElement(aStringBuilder(s.getConstructor()));
		}
		JsonObjectNodeBuilder builder = anObjectBuilder();
		builder
		.withField("tiles", arr)
		.withField("sprites", arr2)
		.withField("name", aStringBuilder(name))
		.withField("width", aNumberBuilder(Integer.toString(width)))
		.withField("height", aNumberBuilder(Integer.toString(height)));
		String f = "";
		if(focus != null) {
			f = focus.getName();
		}
		builder.withField("focus", aStringBuilder(f));
		return builder;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public Tile getTile(int x, int y) {
		if(x>=0 && x<this.width && y>=0 && y<this.height) {
			return tiles[x][y];
		}
		return null;
	}
	
	public void setTile(int x, int y, Tile tile) {
		if(x>=0 && x<this.width && y>=0 && y<this.height) {
			tiles[x][y] = tile;
		}
	}
}
