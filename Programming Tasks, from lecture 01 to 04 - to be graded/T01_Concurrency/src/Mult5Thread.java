
public class Mult5Thread extends QueuedThread {
	private static Mult5Thread instance = new Mult5Thread();
	static {instance.start();}
	public static Mult5Thread getInstance() {
		return instance;
	}
	private Mult5Thread() {
		super();
	}
	@Override
	public void run() {
		while(true) {
			try {
				MergeThread.getInstance().addToQueue(2, inputQueue.take() * 5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
