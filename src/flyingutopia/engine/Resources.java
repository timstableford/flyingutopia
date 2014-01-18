package flyingutopia.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import static argo.jdom.JsonNodeBuilders.*;

public class Resources {
	private static final JsonFormatter JSON_FORMATTER = new PrettyJsonFormatter();
	private static final JdomParser JDOM_PARSER = new JdomParser();
	private ArrayList<Resource> resources;
	private HashMap<String, Resource> resMap;
	public Resources() throws InvalidSyntaxException, FileNotFoundException {
		resources = new ArrayList<Resource>();
		resMap = new HashMap<String, Resource>();
		this.parseJson();
		this.loadImages();
		for(Resource r: resources) {
			for(String s: r.getNames()) {
				resMap.put(s, r);
			}
		}
	}
	
	public void parseJson() throws FileNotFoundException, InvalidSyntaxException {
		Scanner s = new Scanner(new FileInputStream(new File("res/tiles.json")));
		String in = s.useDelimiter("\\A").next();
		s.close();
		JsonRootNode json = JDOM_PARSER.parse(in);
		List<JsonNode> nodes = json.getArrayNode("tiles");
		for(JsonNode node: nodes) {
			resources.add(new Resource(node));
		}
	}
	
	public boolean save() {
		String json = JSON_FORMATTER.format(getJson());
		try {
			FileOutputStream f = new FileOutputStream("res/tiles.json");
			PrintWriter writer = new PrintWriter(f);
			writer.write(json);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public JsonRootNode getJson() {
		JsonArrayNodeBuilder arr = anArrayBuilder();
		for(Resource r: resources) {
			arr.withElement(r.getJson());
		}
		JsonObjectNodeBuilder builder = anObjectBuilder()
				.withField("tiles", arr);
		return builder.build();
	}
	
	public void addResource(Resource r) {
		resources.add(r);
	}
	
	public List<Resource> getResources() {
		return resources;
	}
	
	public Resource getResource(String name) {
		return resMap.get(name);
	}
	
	public void loadImages() {
		for(Resource r: resources) {
			r.loadImage();
		}
	}
}
