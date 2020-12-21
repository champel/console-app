package cat.champel.console;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class ConsoleAppTester {
	private PipedOutputStream userInput;
	private PipedInputStream output;

	public void setup(ConsoleApp consoleApp) throws IOException {
		PipedInputStream in = new PipedInputStream();
		userInput = new PipedOutputStream(in);

		PipedOutputStream pos = new PipedOutputStream();
		output = new PipedInputStream(pos);

		new Thread(consoleApp.override(new Console(in, new PrintStream(pos)))).start();
	}
	
	public void writeCommand(String command) throws IOException {
		userInput.write(command.replace("\\n","\n").getBytes());
		userInput.flush();
	}
	
	public String readOutput(String expected) throws Exception {
		int len = expected.getBytes().length;
		for(int i = 0; i < 100; i++) { 
			if (output.available() >= len) {
				break;
			} else { 
				Thread.sleep(20); 
			} 
		}
		return new String(output.readNBytes(Math.min(output.available(),len)));
	}
}