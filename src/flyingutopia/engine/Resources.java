package flyingutopia.engine;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import static argo.jdom.JsonNodeBuilders.*;

public class Resources {
	private ArrayList<Resource> resources;
	private HashMap<String, Resource> resMap;
	public Resources() throws InvalidSyntaxException, FileNotFoundException {
		resources = new ArrayList<Resource>();
		resMap = new HashMap<String, Resource>();
	}
	
	public void load(JsonNode json) throws FileNotFoundException, InvalidSyntaxException {
		resources = new ArrayList<Resource>();
		resMap = new HashMap<String, Resource>();
		this.parseJson(json);
		this.loadImages();
		for(Resource r: resources) {
			for(String s: r.getNames()) {
				resMap.put(s, r);
			}
		}
	}
	
	private void parseJson(JsonNode json) throws FileNotFoundException, InvalidSyntaxException {
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
