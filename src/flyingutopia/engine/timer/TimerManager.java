package flyingutopia.engine.timer;

import java.util.ArrayList;
import java.util.HashMap;

public class TimerManager {
	private static TimerManager instance;
	private HashMap<Timers, TimerList> timers;
	
	public static void stop() {
		TimerManager.getInstance().stopTimers();
	}
	
	public static void addTimer(Timers type, Timer t) {
		TimerManager.getInstance().timers.get(type).addTimer(t);
	}
	
	public static void reset() {
		for(TimerList t: TimerManager.getInstance().timers.values()) {
			t.reset();
		}
	}
	
	private TimerManager() {
		this.timers = new HashMap<Timers, TimerList>();
		for(Timers t: Timers.values()) {
			TimerList tl = new TimerList(t);
			timers.put(tl.getType(), tl);
		}
	}
	
	private void stopTimers() {
		for(TimerList t: timers.values()) {
			t.stop();
		}
	}
	
	private static TimerManager getInstance() {
		if(instance == null) {
			instance = new TimerManager();
		}
		return instance;
	}
	
	private class TimerList implements Runnable{
		private ArrayList<Timer> timers;
		private Timers type;
		private boolean loop;
		private long lastTick;
		public TimerList(Timers type) {
			this.timers = new ArrayList<Timer>();
			this.type = type;
			this.loop = false;
		}
		
		public void addTimer(Timer t) {
			if(!this.timers.contains(t)) {
				this.timers.add(t);
				if(!loop && this.timers.size() > 0) {
					this.start();
				}
			}
		}
		
		public void reset() {
			this.timers = new ArrayList<Timer>();
		}
		
		public Timers getType() {
			return this.type;
		}
		
		public void stop() {
			this.loop = false;
		}
		
		public void start() {
			if(!loop) {
				this.loop = true;
				(new Thread(this)).start();
			}
		}

		@Override
		public void run() {
			while(loop) {
				long dt = System.currentTimeMillis() - lastTick;
				for(Timer t: timers) {
					t.onTimer(dt);
				}
				lastTick = System.currentTimeMillis();
				try {
					Thread.sleep(this.type.getSleep());
				} catch (InterruptedException e) {
					//Thread was not sleepy, doesn't matter
				}
			}
		}
	}
}
