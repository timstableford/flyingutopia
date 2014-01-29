package flyingutopia.engine.world;

import java.awt.Color;
import java.awt.Graphics;

import flyingutopia.engine.ImageResources;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import static argo.jdom.JsonNodeBuilders.*;

public class Level {
	public static final int COLLISION_RESOLUTION = 2;
	private Tile tiles[][];
	private boolean collisionMap[][];
	private int width;
	private int height;
	private double zoom = 1;
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		collisionMap = new boolean[width * COLLISION_RESOLUTION][height * COLLISION_RESOLUTION];
	}
	
	public void render(Graphics g, int startx, int endx, int starty, int endy) {
		g.setColor(Color.black);
		g.fillRect((int)(startx * ImageResources.TILE_SIZE * zoom), (int)(starty * ImageResources.TILE_SIZE * zoom), 
				(int)(endx * ImageResources.TILE_SIZE * zoom), (int)(endy * ImageResources.TILE_SIZE * zoom));
		for(int x=startx; x<endx && x<this.width; x++) {
			for(int y=starty; y<endy && y<this.height; y++) {
				Tile t = tiles[x][y];
				if(t != null) {
        			if(t.getBackground() != null) {
        				g.drawImage(t.getBackground().getImage().getImage(),
        						(int)(x*ImageResources.TILE_SIZE*zoom),
        						(int)(y*ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom), null);
        			}
        			if(t.getResource() != null) {
        				g.drawImage(t.getResource().getImage().getImage(),
        						(int)(x*ImageResources.TILE_SIZE*zoom),
        						(int)(y*ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom), null);
        			}
        		}
			}
		}
	}
	
	public void generateCollisionMap() {
		for(int x=0; x<this.width * COLLISION_RESOLUTION; x++) {
			for(int y=0; y<this.height * COLLISION_RESOLUTION; y++) {
				collisionMap[x][y] = false;
			}
		}
	
		for(int x=0; x<this.width; x++) {
			for(int y=0; y<this.height; y++) {
				Tile t = tiles[x][y];
				if(t != null) {
					boolean tileMap[][] = t.getCollisionMap(COLLISION_RESOLUTION);
					for(int ax=0; ax<COLLISION_RESOLUTION; ax++) {
						for(int ay=0; ay<COLLISION_RESOLUTION; ay++) {
							collisionMap[x * COLLISION_RESOLUTION + ax][y * COLLISION_RESOLUTION + ay] = tileMap[ax][ay];
						}
					}
				}
			}
		}
	}
	
	public boolean isColliding(double cx, double cy) {
		if(collisionMap == null) {
			return false;
		}
		int x = (int) (cx/(ImageResources.TILE_SIZE / COLLISION_RESOLUTION));
		int y = (int) (cy/(ImageResources.TILE_SIZE / COLLISION_RESOLUTION));
		return collisionMap[x][y];
	}
	
	public boolean[][] getCollisionMap() {
		return collisionMap;
	}
	
	public void render(Graphics g) {
		this.render(g, 0, this.width - 1, 0, this.height - 1);
	}
	
	public Level(JsonNode rootNode) {
		width = Integer.parseInt(rootNode.getNumberValue("width"));
		height = Integer.parseInt(rootNode.getNumberValue("height"));
		tiles = new Tile[width][height];
		collisionMap = new boolean[width * COLLISION_RESOLUTION][height * COLLISION_RESOLUTION];
		for(JsonNode n: rootNode.getArrayNode("tiles")) {
			Tile t = new Tile(n);
			tiles[t.getX()][t.getY()] = t;
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
