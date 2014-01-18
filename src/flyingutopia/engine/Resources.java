package flyingutopia.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import argo.jdom.JdomParser;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import static argo.jdom.JsonNodeBuilders.*;

public class Resources {
	private static final JdomParser JDOM_PARSER = new JdomParser();
	private ArrayList<Resource> resources;
	public Resources() {
		resources = new ArrayList<Resource>();
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
	
	public void loadImages() {
		for(Resource r: resources) {
			r.loadImage();
		}
	}
}