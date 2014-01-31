package flyingutopia.engine.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.saj.InvalidSyntaxException;
import flyingutopia.engine.Engine;
import flyingutopia.engine.ImageResources;
import flyingutopia.engine.Player;
import flyingutopia.engine.interactions.ActionParser;
import flyingutopia.engine.timer.TimerManager;
import flyingutopia.engine.timer.Timers;
import flyingutopia.engine.world.Level;

public class FlyingUtopia extends JFrame implements SelectionListener{
	private static final long serialVersionUID = -3349917878924010552L;
	public static final JsonFormatter JSON_FORMATTER = new PrettyJsonFormatter();
	public static final JdomParser JDOM_PARSER = new JdomParser();
	public static final String TILES_FILE = "tiles.json";
	private static FlyingUtopia instance;
	private Engine engine;
	public FlyingUtopia() throws FileNotFoundException {
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		engine = new Engine();
		InputStream is = new FileInputStream(new File("res/levels/test.json"));
		Scanner s = new Scanner(is);
		String json = s.useDelimiter("\\A").next();
		s.close();
		try {
			JsonNode node = JDOM_PARSER.parse(json);
			engine.setLevel(new Level(node));
			engine.getLevel().generateCollisionMap();
		} catch (InvalidSyntaxException e) {
			System.err.println("Could not load level");
			System.exit(-1);
		}
		Player p = new Player(112,80);
		engine.addCollidable(p);
		engine.getLevel().addSprite(p);
		engine.setFocus(p);
		engine.addKeyListener(p);
		TimerManager.addTimer(Timers.MAIN, p);
	}
	
	public void setContent(JPanel content) {
		this.getContentPane().removeAll();
		this.getContentPane().add(content);
		this.revalidate();
	}
	
	@Override
	public void onSelected(MenuOption option) {
		if(option.getAction().equals("start")) {
			this.setContent(engine);
			engine.repaint();
		}
	}
	
	public Engine getEngine() {
		return this.engine;
	}
	
	public static FlyingUtopia getInstance() {
		return instance;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		ActionParser.getInstance();
		try {
		    InputStream resStream = new FileInputStream(new File("res/"+TILES_FILE));
		    Scanner scan = new Scanner(resStream);
		    String str = scan.useDelimiter("\\A").next();
		    scan.close();
		    ImageResources.getInstance().load(JDOM_PARSER.parse(str));
		} catch (FileNotFoundException | InvalidSyntaxException e) {
			System.err.println("Could not generate resources");
			System.exit(-1);
		}
		instance = new FlyingUtopia();
		Menu m = new Menu();
		m.setListener(instance);
		instance.setContent(m);
	}

}
