package flyingutopia.gui;

import java.util.ArrayList;

public class Menu extends MenuScreen{
	private static final long serialVersionUID = 2374501037208063966L;
	private ArrayList<MenuOption> mainMenu;
	private ArrayList<MenuOption> loadMenu;
	public Menu() {
		super();
		mainMenu = new ArrayList<MenuOption>();
		mainMenu.add(new MenuOption("Start New", "start"));
		mainMenu.add(new MenuOption("Load", "goto", "load"));
		mainMenu.add(new MenuOption("Level Editor", "editor"));
		
		loadMenu = new ArrayList<MenuOption>();
		loadMenu.add(new MenuOption("Back", "goto", "main"));
		loadMenu.add(new MenuOption("Slot 1", "load", "slot1"));
		loadMenu.add(new MenuOption("Slot 2", "load", "slot2"));
		loadMenu.add(new MenuOption("Slot 3", "load", "slot3"));

		this.options = mainMenu;
	}

	@Override
	public void onEnter() {
		MenuOption o = options.get(selected);
		if(o != null) {
			if(o.getAction().equals("goto")) {
				this.reset();
				if(o.getAttribute().equals("main")) {
					this.options = mainMenu;
				}else if(o.getAttribute().equals("load")) {
					this.options = loadMenu;
					this.select(0);
				}
			} else if (listener != null) {
				listener.onSelected(o);
			}
		}
	}
}
