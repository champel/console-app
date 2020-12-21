package cat.champel.console;

import java.util.Optional;

public interface CommandBuilder {
	Optional<Command> build(String userCommand);
}
