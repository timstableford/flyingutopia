package flyingutopia.engine.interactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public class ActionParser {
	public static final String PARAMETER_SPLIT_C = "|";
	public static final String PARAMETER_SPLIT = "\\|";
	public static final String ACTION_SPLIT = "~";
	private static ActionParser instance = null;
	private HashMap<String, WorldAction> actions;
	public static ActionParser getInstance() {
		if(instance == null) {
			instance = new ActionParser();
		}
		return instance;
	}
	public ActionParser() {
		actions = new HashMap<String, WorldAction>();
		Iterator<WorldAction> worldActions = ServiceLoader.load(WorldAction.class).iterator();
        while(worldActions.hasNext()) {
                try {
                        WorldAction a = worldActions.next();
                        actions.put(a.getName(), a);
                } catch (ServiceConfigurationError e){
                        System.err.println("Could not load a world action");
                }
        }
	}
	public static void registerAction(WorldAction source) {
		getInstance().actions.put(source.getName(), source);
	}
	public static List<WorldAction> parseActions(String action, String attribute) {
		ArrayList<WorldAction> act = new ArrayList<WorldAction>();

		if(action.length()>0) {
			String[] sp1 = { action };
			String[] sa1 = { attribute };
			if(action.contains(ACTION_SPLIT) && attribute.contains(ACTION_SPLIT)) {
				sp1 = action.split(ACTION_SPLIT);
				sa1 = attribute.split(ACTION_SPLIT);
			}
			for(int i=0; i<sp1.length; i++) {
				sp1[i] = sp1[i].trim();
				sa1[i] = sa1[i].trim();
				if(sp1[i].length()>0 && sp1[i].contains(PARAMETER_SPLIT_C)) {
					String[] sp2 = sp1[i].split(PARAMETER_SPLIT);
					if(getInstance().actions.containsKey(sp2[1])) {
						WorldAction w = getInstance().actions.get(sp2[1]).getClone();
						w.setInteraction(sp2[0]);
						String[] p = {sa1[i].trim()};
						if(sa1[i].contains(PARAMETER_SPLIT_C)) {
							p = sa1[i].split(PARAMETER_SPLIT);
						}
						w.parseAttributes(p);
						act.add(w);
					}
				}
			}
		}
		
		return act;
	}
}
