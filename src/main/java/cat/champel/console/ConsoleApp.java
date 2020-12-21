package cat.champel.console;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConsoleApp implements Runnable {
	public static final String EXIT_COMMAND = "exit";
	public static final String BYE_MESSAGE = "bye";

	private final Console console;
	private final CommandExecutor commandExecutor;
	private final View view;

	ConsoleApp(Console console, CommandExecutor commandExecutor, View view) {
		this.console = console;
		this.commandExecutor = commandExecutor;
		this.view = view;
	}
	
	public ConsoleApp(Console console, List<CommandBuilder> commandBuilders, Map<Class<?>, Function<Object, String>> formatters) {
		this(
			console,
			new CommandExecutor(new CommandsFactory(commandBuilders)),
			new View(formatters));
	}
	
	public ConsoleApp(List<CommandBuilder> commandBuilders, Map<Class<?>, Function<Object, String>> formatters) {
		this(new Console(System.in, System.out), commandBuilders, formatters);
	}
	
	ConsoleApp override(Console console) {
		return new ConsoleApp(console, commandExecutor, view);
	}

	@Override
	public void run() {
		try {
			String userCommand = console.readLine();
			while(!EXIT_COMMAND.equals(userCommand)) {
				commandExecutor.execute(userCommand)
					.flatMap(view::display)
					.ifPresent(console::write); 
				userCommand = console.readLine();
			}
			console.write(BYE_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
