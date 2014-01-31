package flyingutopia.engine;

import static argo.jdom.JsonNodeBuilders.anObjectBuilder;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonObjectNodeBuilder;
import static argo.jdom.JsonNodeBuilders.*;

public class ImageResource {
	private String name;
	private String filename;
	private HashMap<String, Integer> frames;
	private List<String> names;
	private ImageFrame[] imageFrames;
	private int currentFrame;
	private long lastUpdate;
	public ImageResource(JsonNode node) {
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
		
		lastUpdate = System.currentTimeMillis();
		
		currentFrame = 0;
		
		names = new ArrayList<String>();
		names.add(name);
		for(Entry<String, Integer> value: frames.entrySet()) {
			names.add(name+"_"+value.getKey());
		}
		System.out.println(name);
	}
	public ImageResource(String name, String filename, HashMap<String, Integer> frames) {
		this.name = name;
		this.filename = filename;
		this.frames = frames;
		this.names = new ArrayList<String>();
		names.add(name);
	}

	public boolean animate() {
		if(getImage().length > 1) {
			if((System.currentTimeMillis() - lastUpdate) > imageFrames[currentFrame].getDelay()) {
				lastUpdate = System.currentTimeMillis();
				return incrementFrame();
			}
		}
		return false;
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public void setCurrentFrame(int frame) {
		if(frame > imageFrames.length) {
			frame = 0;
		}
		currentFrame = frame;
	}
	
	public boolean incrementFrame() {
		currentFrame++;
		if(currentFrame >= imageFrames.length) {
			currentFrame = imageFrames.length - 1;
			if(getImage()[currentFrame].getDisposal().equals("None")) {
				currentFrame = 0;
				return false;
			}
		}
		return true;
	}
	
	public ImageResource(String name, String filename) {
		this(name, filename, new HashMap<String, Integer>());
	}
	public ImageFrame[] getImage() {
		return imageFrames;
	}
	public List<String> getNames() {
		return names;
	}
	public boolean loadImage() {

		String newFilename = filename;
		//Try and load it as a file
		if(!(new File(filename).exists())) {
			File f = new File("."); // current directory
			File[] files = f.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					File child = new File(file.getPath()+System.getProperty("file.separator")+filename);
					if(child.exists()) {
						newFilename = child.getPath();
					}
				}
			}
			if(newFilename == filename) {
				ClassLoader cldr = this.getClass().getClassLoader();
				InputStream is = cldr.getResourceAsStream(filename);
				try {
					this.imageFrames = this.readGIF(is);
				} catch (IOException e) {
					return false;
				}
			} else {
				try {
					InputStream is = new FileInputStream(newFilename);
					this.imageFrames = this.readGIF(is);
				} catch (IOException e) {
					return false;
				}
			}
		}
		return true;
	}
	public String getName() {
		return name;
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
	
	private ImageFrame[] readGIF(InputStream is) throws IOException {
		ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
		reader.setInput(ImageIO.createImageInputStream(is));
	    ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);

	    int width = -1;
	    int height = -1;

	    IIOMetadata metadata = reader.getStreamMetadata();
	    if (metadata != null) {
	        IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

	        NodeList globalScreenDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");

	        if (globalScreenDescriptor != null && globalScreenDescriptor.getLength() > 0) {
	            IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreenDescriptor.item(0);

	            if (screenDescriptor != null) {
	                width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
	                height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
	            }
	        }
	    }

	    BufferedImage master = null;
	    Graphics2D masterGraphics = null;

	    for (int frameIndex = 0;; frameIndex++) {
	        BufferedImage image;
	        try {
	            image = reader.read(frameIndex);
	        } catch (IndexOutOfBoundsException io) {
	            break;
	        }

	        if (width == -1 || height == -1) {
	            width = image.getWidth();
	            height = image.getHeight();
	        }

	        IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
	        IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
	        int delay = Integer.valueOf(gce.getAttribute("delayTime"));
	        String disposal = gce.getAttribute("disposalMethod");

	        int x = 0;
	        int y = 0;

	        if (master == null) {
	            master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            masterGraphics = master.createGraphics();
	            masterGraphics.setBackground(new Color(0, 0, 0, 0));
	        } else {
	            NodeList children = root.getChildNodes();
	            for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
	                Node nodeItem = children.item(nodeIndex);
	                if (nodeItem.getNodeName().equals("ImageDescriptor")) {
	                    NamedNodeMap map = nodeItem.getAttributes();
	                    x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
	                    y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
	                }
	            }
	        }
	        masterGraphics.drawImage(image, x, y, null);

	        BufferedImage copy = new BufferedImage(master.getColorModel(), master.copyData(null), master.isAlphaPremultiplied(), null);
	        frames.add(new ImageFrame(copy, delay, disposal));

	        if (disposal.equals("restoreToPrevious")) {
	            BufferedImage from = null;
	            for (int i = frameIndex - 1; i >= 0; i--) {
	                if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0) {
	                    from = frames.get(i).getImage();
	                    break;
	                }
	            }

	            master = new BufferedImage(from.getColorModel(), from.copyData(null), from.isAlphaPremultiplied(), null);
	            masterGraphics = master.createGraphics();
	            masterGraphics.setBackground(new Color(0, 0, 0, 0));
	        } else if (disposal.equals("restoreToBackgroundColor")) {
	            masterGraphics.clearRect(x, y, image.getWidth(), image.getHeight());
	        }
	    }
	    reader.dispose();

	    return frames.toArray(new ImageFrame[frames.size()]);
	}
}
