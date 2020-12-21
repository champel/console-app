package cat.champel.console;

import java.util.Optional;

public interface Command {
	Optional<Object> execute();
}
