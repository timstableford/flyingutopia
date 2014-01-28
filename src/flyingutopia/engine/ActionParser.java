package flyingutopia.engine;

import java.util.HashMap;

public class ActionParser {
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
	}
	public static void registerAction(WorldAction source) {
		getInstance().actions.put(source.getName(), source);
	}
	public static HashMap<String, WorldAction> parseActions(String action, String attribute) {
		HashMap<String, WorldAction> act = new HashMap<String, WorldAction>();
		String[] sp1 = new String[1];
		String[] sa1 = new String[1];
		sp1[0] = action;
		sa1[0] = attribute;
		if(action.contains("~") && attribute.contains("~")) {
			sp1 = action.split("~");
			sa1 = attribute.split("~");
		}
		for(int i=0; i<sp1.length; i++) {
			sp1[i] = sp1[i].trim();
			sa1[i] = sa1[i].trim();
			if(sp1[i].length()>0 && sp1[i].contains("|")) {
				String[] sp2 = sp1[i].split("|", 2);
				if(getInstance().actions.containsKey(sp2[1])) {
					WorldAction w = getInstance().actions.get(sp2[1]).getClone();
					w.setInteraction(sp2[0]);
					w.parseAttributes(sa1[i]);
					act.put(w.getName(), w);
				}
			}
		}
		return act;
	}
}
