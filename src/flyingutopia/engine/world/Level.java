package flyingutopia.engine.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import flyingutopia.engine.ImageResources;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import static argo.jdom.JsonNodeBuilders.*;

public class Level implements ImageObserver{
	public static final int COLLISION_RESOLUTION = 2;
	private Tile tiles[][];
	private int width;
	private int height;
	private double zoom = 1;
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
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
        				t.getBackground().animate();
        				g.drawImage(t.getBackground().getImage()[t.getBackground().getCurrentFrame()].getImage(),
        						(int)(x*ImageResources.TILE_SIZE*zoom),
        						(int)(y*ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom), this);
        			}
        			if(t.getResource() != null) {
        				t.getResource().animate();
        				g.drawImage(t.getResource().getImage()[t.getResource().getCurrentFrame()].getImage(),
        						(int)(x*ImageResources.TILE_SIZE*zoom),
        						(int)(y*ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom),
        						(int)(ImageResources.TILE_SIZE*zoom), this);
        			}
        		}
			}
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

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		return true;
	}
}
