package cat.champel.console;

import java.util.Optional;

class CommandExecutor {

	private final CommandsFactory commandsFactory;

	public CommandExecutor(CommandsFactory commandFactory) {
		this.commandsFactory = commandFactory;
	}

	public Optional<Object> execute(String userCommand) {
		return commandsFactory.commandsFor(userCommand).stream()
				.map(Command::execute)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst();
	}

}
