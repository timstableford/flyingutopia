package flyingutopia.engine.timer;

public enum Timers {
	MAIN(30),
	WORLD(40);
	private final int sleep;
	private Timers(final int sleep) {
		this.sleep = sleep;
	}
	public int getSleep() {
		return this.sleep;
	}
}
