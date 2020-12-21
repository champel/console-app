package cat.champel.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;;

public class Console {
	public static final String PROMPT = "> ";
	private final Scanner scanner;
	private final PrintStream out;
	
	public Console(InputStream in, PrintStream out) {
		this.out = out;
		this.scanner = new Scanner(in);
	}
	
	public String readLine() {
		out.print(PROMPT);
		return scanner.nextLine();
	}
	
	public void write(String output) {
		out.println(output);
	}
}
