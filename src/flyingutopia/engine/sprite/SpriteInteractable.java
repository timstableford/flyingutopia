package flyingutopia.engine.sprite;


public interface SpriteInteractable {
	public void onCollision(Sprite source);
	public void onSeperate(Sprite source);
	public void onInteract(Sprite source);
}
