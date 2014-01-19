package flyingutopia.engine.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import flyingutopia.engine.ImageResources;

public class MenuScreen extends JPanel implements KeyListener{
	protected static final int INSET = 10;
	protected static final int ITEM_HEIGHT = 32;
	private static final long serialVersionUID = -463446655847128691L;
	protected ArrayList<MenuOption> options;
	protected int selected = 0;
	protected SelectionListener listener;
	public MenuScreen() {
		options = new ArrayList<MenuOption>();
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		listener = null;
	}
	
	public void reset() {
		options = new ArrayList<MenuOption>();
		select(0);
	}
	
	public void addOption(MenuOption option) {
		this.options.add(option);
	}
	
	public void setListener(SelectionListener l) {
		this.listener = l;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		ImageIcon menuPanel = ImageResources.getInstance().getResource("menu_panel").getImage();
		ImageIcon menuArrow = ImageResources.getInstance().getResource("menu_arrow").getImage();
		
		g.drawImage(menuPanel.getImage(), this.getWidth()/2 - menuPanel.getIconWidth()/2,
				this.getHeight()/2 - menuPanel.getIconHeight()/2, null);
		
		g.setColor(Color.black);
		g.setFont(g.getFont().deriveFont(18.0f));
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		for(int y=0; y<options.size(); y++) {
			int textHeight = this.getHeight()/2 - menuPanel.getIconHeight()/2 + INSET*4 + ITEM_HEIGHT*y;
			if(y == selected) {
				g.drawImage(menuArrow.getImage(), this.getWidth()/2 - menuPanel.getIconWidth()/2 + INSET,
						textHeight + metrics.getHeight()/2 - menuArrow.getIconHeight(), null);
			}
			g.drawString(options.get(y).getText(),
					this.getWidth()/2 - menuPanel.getIconWidth()/2 + INSET*2 + menuArrow.getIconWidth(),
					textHeight);
		}
		
	}
	
	public void select(int option) {
		selected = option;
		if(selected >= options.size()) {
			selected = options.size() - 1;
		}else if(selected < 0) {
			selected = 0;
		}
		repaint();
	}
	
	public void onEnter() {
		MenuOption o = options.get(selected);
		if(listener != null && o != null) {
			listener.onSelected(options.get(selected));
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			select(selected+1);
			break;
		case KeyEvent.VK_UP:
			select(selected-1);
			break;
		case KeyEvent.VK_ENTER:
			onEnter();
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}
}
