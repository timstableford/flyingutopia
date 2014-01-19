package flyingutopia.engine.gui;

public class MenuOption {
	private String action, attribute, text;
	public MenuOption(String text, String action, String attribute) {
		this.attribute = attribute;
		this.action = action;
		this.text = text;
	}
	public MenuOption(String text, String action) {
		this(text, action, null);
	}
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
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
