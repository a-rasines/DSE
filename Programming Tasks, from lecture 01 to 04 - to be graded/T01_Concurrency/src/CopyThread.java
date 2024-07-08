
public class CopyThread extends QueuedThread {
	private static CopyThread instance = new CopyThread();
	static {instance.start();}
	public static CopyThread getInstance() {
		return instance;
	}
	private CopyThread() {
		super();
	}
	@Override
	public void run() {
		while (true) {
			try {
				int elem = inputQueue.take();
				PrintThread.getInstance().addToQueue(elem);
				Mult2Thread.getInstance().addToQueue(elem);
				Mult3Thread.getInstance().addToQueue(elem);
				Mult5Thread.getInstance().addToQueue(elem);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
