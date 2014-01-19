package flyingutopia.engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import flyingutopia.engine.world.Level;

public class Engine extends JPanel{
	private static final long serialVersionUID = -2525142042121440587L;
	private Level level;
	double x,y,zoom;

	public Engine() {
		x = 0;
		y = 0;
		zoom = 2;
	}

	public void setLevel(Level level) {
		this.level = level;
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if(this.level != null) {
			BufferedImage buffer = new BufferedImage(level.getWidth() * ImageResources.TILE_SIZE,
					level.getHeight() * ImageResources.TILE_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
			this.level.render(buffer.getGraphics());
			
			g.drawImage(buffer, 0, 0, this.getWidth(), this.getHeight(),
					(int)(x * zoom), (int)(y * zoom), (int)((x + this.getWidth())/zoom), (int)((y + this.getHeight())/zoom), null);
		}
	}

}
