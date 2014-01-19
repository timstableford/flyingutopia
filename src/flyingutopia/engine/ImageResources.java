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

public class ImageResources {
	private ArrayList<ImageResource> resources;
	private HashMap<String, ImageResource> resMap;
	public ImageResources() throws InvalidSyntaxException, FileNotFoundException {
		resources = new ArrayList<ImageResource>();
		resMap = new HashMap<String, ImageResource>();
	}
	
	public void load(JsonNode json) throws FileNotFoundException, InvalidSyntaxException {
		resources = new ArrayList<ImageResource>();
		resMap = new HashMap<String, ImageResource>();
		this.parseJson(json);
		this.loadImages();
		for(ImageResource r: resources) {
			for(String s: r.getNames()) {
				resMap.put(s, r);
			}
		}
	}
	
	private void parseJson(JsonNode json) throws FileNotFoundException, InvalidSyntaxException {
		List<JsonNode> nodes = json.getArrayNode("tiles");
		for(JsonNode node: nodes) {
			resources.add(new ImageResource(node));
		}
	}
	
	public JsonRootNode getJson() {
		JsonArrayNodeBuilder arr = anArrayBuilder();
		for(ImageResource r: resources) {
			arr.withElement(r.getJson());
		}
		JsonObjectNodeBuilder builder = anObjectBuilder()
				.withField("tiles", arr);
		return builder.build();
	}
	
	public void removeResource(ImageResource r) {
		if(resources.contains(r)) {
			resources.remove(r);
			for(String s: r.getNames()) {
				if(resMap.containsKey(s)) {
					resMap.remove(s);
				}
			}
		}
	}
	
	public void addResource(ImageResource r) {
		if(!resMap.containsKey(r.getName())) {
			resources.add(r);
			for(String s: r.getNames()) {
				resMap.put(s, r);
			}
		}
	}
	
	public List<ImageResource> getResources() {
		return resources;
	}
	
	public ImageResource getResource(String name) {
		return resMap.get(name);
	}
	
	public void loadImages() {
		for(ImageResource r: resources) {
			r.loadImage();
		}
	}
}
