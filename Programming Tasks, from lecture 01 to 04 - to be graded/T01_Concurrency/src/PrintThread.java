public class PrintThread extends QueuedThread{
	private static PrintThread instance = new PrintThread();
	static {instance.start();}
	public static PrintThread getInstance() {
		return instance;
	}
	private PrintThread() {
		super();
	}
	public void run() {
		while (true)
			try {
				System.out.println(inputQueue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

}
