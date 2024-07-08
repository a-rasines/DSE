
public class Mult3Thread extends QueuedThread {
	private static Mult3Thread instance = new Mult3Thread();
	static {instance.start();}
	public static Mult3Thread getInstance() {
		return instance;
	}
	private Mult3Thread() {
		super();
	}
	@Override
	public void run() {
		while(true) {
			try {
				MergeThread.getInstance().addToQueue(1, inputQueue.take() * 3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
