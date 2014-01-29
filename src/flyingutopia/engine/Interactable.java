package flyingutopia.engine;

public interface Interactable {
	public void onCollision(Sprite source);
	public void onSeperate(Sprite source);
	public void onInteract(Sprite source);
}
