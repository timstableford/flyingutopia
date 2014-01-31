package flyingutopia.engine.vector;

public enum Direction {
	LEFT("left"),
	RIGHT("right"),
	UP("up"),
	DOWN("down");
	private Direction(final String text) {
        this.text = text;
    }
	private final String text;
	@Override
    public String toString() {
        return text;
    }
}
