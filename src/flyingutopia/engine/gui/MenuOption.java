package flyingutopia.engine.gui;

public class MenuOption {
	private String action;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private String text;
	public MenuOption(String text, String action) {
		this.action = action;
		this.text = text;
	}
	
}
