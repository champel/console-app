package cat.champel.console;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class CommandsFactory {
	private final List<CommandBuilder> commandBuilders;

	public CommandsFactory(List<CommandBuilder> commandBuilders) {
		this.commandBuilders = commandBuilders;
		
	}

	public List<Command> commandsFor(String userCommand) {
		return commandBuilders.stream()
				.map(builder -> builder.build(userCommand))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}
}
