package flyingutopia.engine;

import static argo.jdom.JsonNodeBuilders.anObjectBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.ImageIcon;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import static argo.jdom.JsonNodeBuilders.*;

public class Resource {
	private String name;
	private String filename;
	private HashMap<String, Integer> frames;
	private ImageIcon image;
	private List<String> names;
	public Resource(JsonNode node) {
		frames = new HashMap<String, Integer>();
		name = node.getStringValue("name");
		filename = node.getStringValue("filename");
		if(node.isArrayNode("frames")) {
			List<JsonNode> fr = node.getArrayNode("frames");
			for(JsonNode n: fr) {
				int id = Integer.parseInt(n.getNumberValue("id"));
				String s = n.getStringValue("name");
				frames.put(s, id);
			}
		}
		
		names = new ArrayList<String>();
		names.add(name);
		for(Entry<String, Integer> value: frames.entrySet()) {
			names.add(name+"_"+value.getKey());
		}
		System.out.println(name);
		System.out.println(filename);
	}
	public Resource(String name, String filename, HashMap<String, Integer> frames) {
		this.name = name;
		this.filename = filename;
		this.frames = frames;
	}
	public Resource(String name, String filename) {
		this(name, filename, new HashMap<String, Integer>());
	}
	public ImageIcon getImage() {
		return image;
	}
	public List<String> getNames() {
		return names;
	}
	public void loadImage() {
		ClassLoader cldr = this.getClass().getClassLoader();
	    java.net.URL imageURL = cldr.getResource("res/tiles/"+filename);
	    image = new ImageIcon(imageURL);
	}
	public JsonObjectNodeBuilder getJson() {
		JsonArrayNodeBuilder arr = anArrayBuilder();
		for(Entry<String, Integer> value: frames.entrySet()) {
			arr.withElement(anObjectBuilder()
					.withField("id", aNumberBuilder(value.getValue().toString()))
					.withField("name", aStringBuilder(value.getKey())));
		}
		JsonObjectNodeBuilder builder = anObjectBuilder()
				.withField("name", aStringBuilder(name))
				.withField("filename", aStringBuilder(filename))
				.withField("frames", arr);
		return builder;
	}
}
