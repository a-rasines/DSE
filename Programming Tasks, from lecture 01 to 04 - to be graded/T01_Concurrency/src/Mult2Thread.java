
public class Mult2Thread extends QueuedThread {
	private static Mult2Thread instance = new Mult2Thread();
	static {instance.start();}
	public static Mult2Thread getInstance() {
		return instance;
	}
	private Mult2Thread() {
		super();
	}
	@Override
	public void run() {
		while(true) {
			try {
				MergeThread.getInstance().addToQueue(0, inputQueue.take() * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
