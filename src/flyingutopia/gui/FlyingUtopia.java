package flyingutopia.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.saj.InvalidSyntaxException;
import flyingutopia.engine.Engine;
import flyingutopia.engine.interactions.ActionParser;
import flyingutopia.engine.resources.ImageResources;
import flyingutopia.engine.world.Level;
import flyingutopia.gui.editor.Editor;

public class FlyingUtopia extends JFrame implements SelectionListener{
	private static final long serialVersionUID = -3349917878924010552L;
	public static final JsonFormatter JSON_FORMATTER = new PrettyJsonFormatter();
	public static final JdomParser JDOM_PARSER = new JdomParser();
	public static final String TILES_FILE = "tiles.json";
	private static FlyingUtopia instance;
	private Engine engine;
	public FlyingUtopia() {
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void load() {
		engine = new Engine();
		try {
			InputStream is = new FileInputStream(new File("res/levels/test.json"));
			JsonNode node = JDOM_PARSER.parse(new InputStreamReader(is));
			is.close();
			engine.setLevel(new Level(node));
		} catch (InvalidSyntaxException | IOException e) {
			System.err.println("Could not load level");
			System.exit(-1);
		}
	}
	
	public void setContent(JPanel content) {
		this.getContentPane().removeAll();
		this.getContentPane().add(content);
		this.revalidate();
	}
	
	@Override
	public void onSelected(MenuOption option) {
		if(option.getAction().equals("start")) {
			load();
			this.setContent(engine);
			engine.repaint();
		}else if(option.getAction().equals("editor")) {
			try {
				new Editor();
			} catch (FileNotFoundException | InvalidSyntaxException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			this.dispose();
		}
	}
	
	public Engine getEngine() {
		return this.engine;
	}

	public static FlyingUtopia getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
		if(args.length > 0 && "-editor".equals(args[0])) {
			try {
				new Editor();
			} catch (FileNotFoundException | InvalidSyntaxException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			return;
		}
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
