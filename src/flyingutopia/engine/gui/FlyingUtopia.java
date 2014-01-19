package flyingutopia.engine.gui;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.saj.InvalidSyntaxException;
import flyingutopia.engine.ImageResources;

public class FlyingUtopia extends JFrame implements SelectionListener{
	private static final long serialVersionUID = -3349917878924010552L;
	public static final JsonFormatter JSON_FORMATTER = new PrettyJsonFormatter();
	public static final JdomParser JDOM_PARSER = new JdomParser();
	public static final String TILES_FILE = "tiles.json";
	public FlyingUtopia() {
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void setContent(JPanel content) {
		this.getContentPane().removeAll();
		this.getContentPane().add(content);
		this.revalidate();
	}
	
	@Override
	public void onSelected(MenuOption option) {
		System.out.println("option selected: "+option.getText());
	}
	
	public static void main(String[] args) {
		try {
			ClassLoader cldr = FlyingUtopia.class.getClassLoader();
		    InputStream resStream = cldr.getResourceAsStream(TILES_FILE);
		    Scanner scan = new Scanner(resStream);
		    String str = scan.useDelimiter("\\A").next();
		    scan.close();
		    ImageResources.getInstance().load(JDOM_PARSER.parse(str));
		} catch (FileNotFoundException | InvalidSyntaxException e) {
			System.err.println("Could not generate resources");
			System.exit(-1);
		}
		FlyingUtopia fu = new FlyingUtopia();
		Menu m = new Menu();
		m.setListener(fu);
		fu.setContent(m);
	}

}
