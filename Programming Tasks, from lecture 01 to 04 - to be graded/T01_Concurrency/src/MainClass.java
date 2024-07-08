import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class MainClass {
	public static void main(String[] args) throws IOException {
		File f = new File("output.log");
		f.createNewFile();
		System.setOut(new PrintStream(f));
		CopyThread.getInstance().addToQueue(1);
	}
}
