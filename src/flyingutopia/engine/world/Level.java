package flyingutopia.engine.world;

public class Level {
	private Tile tiles[][];
	private int width;
	private int height;
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		
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
		return tiles[x][y];
	}
	
	public void setTile(int x, int y, Tile tile) {
		if(x>=0 && x<this.width && y>=0 && y<this.height) {
			tiles[x][y] = tile;
		}
	}
}
