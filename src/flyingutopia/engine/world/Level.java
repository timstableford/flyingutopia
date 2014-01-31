package flyingutopia.engine.world;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import flyingutopia.engine.ImageResources;
import flyingutopia.engine.Sprite;
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
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		sprites = new ArrayList<Sprite>();
	}
	
	public void addSprite(Sprite s) {
		this.sprites.add(s);
	}
	
	public void removeSprite(Sprite s) {
		this.sprites.remove(s);
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
		endx = checkInt(this.getWidth(), endx);
		startx = checkInt(endx, startx);
		endy = checkInt(this.getHeight(), endy);
		starty = checkInt(endy, starty);
		g.setColor(Color.black);
		g.fillRect((int)(startx * ImageResources.TILE_SIZE), (int)(starty * ImageResources.TILE_SIZE), 
				(int)(endx * ImageResources.TILE_SIZE), (int)(endy * ImageResources.TILE_SIZE));
		for(int x=startx; x<endx && x<this.width; x++) {
			for(int y=starty; y<endy && y<this.height; y++) {
				Tile t = tiles[x][y];
				if(t != null) {
        			t.render(g);
        		}
			}
		}
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
		for(JsonNode n: rootNode.getArrayNode("tiles")) {
			Tile t = new Tile(n);
			tiles[t.getX()][t.getY()] = t;
		}
		sprites = new ArrayList<Sprite>();
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
		JsonObjectNodeBuilder builder = anObjectBuilder();
		builder
		.withField("tiles", arr)
		.withField("width", aNumberBuilder(Integer.toString(width)))
		.withField("height", aNumberBuilder(Integer.toString(height)));
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
