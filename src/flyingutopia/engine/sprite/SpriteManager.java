package flyingutopia.engine.sprite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public class SpriteManager {
	private static SpriteManager instance;
	private HashMap<String, Sprite> sprites;

	private static SpriteManager getInstance() {
		if(instance == null) {
			instance = new SpriteManager();
		}
		return instance;
	}
	
	public static Sprite getSprite(String con) {
		String type = con.substring(0, con.indexOf('('));
		String constructors[] = { con.substring(con.indexOf('(') + 1, con.indexOf(')')).trim() };
		if(constructors[0].contains(",")) {
			constructors = constructors[0].split(",");
		}
		if(getInstance().sprites.containsKey(type)) {
			return getInstance().sprites.get(type).newInstance(constructors);
		}
		return null;
	}
	
	public SpriteManager() {
		sprites = new HashMap<String, Sprite>();
		System.out.println("Loading sprites...");
		Iterator<Sprite> spriteClasses = ServiceLoader.load(Sprite.class).iterator();
		while(spriteClasses.hasNext()) {
			try {
				Sprite a = spriteClasses.next();
				System.out.println("Found "+a.getName());
				sprites.put(a.getName(), a);
			} catch (ServiceConfigurationError e){
				System.err.println("Could not load a world action");
			}
		}
		System.out.println("Done.");
	}
}
