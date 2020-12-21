package cat.champel.console;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandExecutorShould {
	
	private static final String USER_COMMAND = "Alice -> Hello!";

	@Mock Command command1;
	@Mock Command command2;
	@Mock CommandsFactory consoleCommandsFactory;

	CommandExecutor commandExecutor;
	
	@BeforeEach
	void setup() {
		commandExecutor = new CommandExecutor(consoleCommandsFactory);
	}

	@Test
	void request_commands_to_the_commands_factory() {
		commandExecutor.execute(USER_COMMAND);
		verify(consoleCommandsFactory).commandsFor(USER_COMMAND);
	}

	@Test
	void execute_all_commands_returned_by_the_factory_on_execute() {
		given(consoleCommandsFactory.commandsFor(USER_COMMAND)).willReturn(List.of(command1, command2));
		commandExecutor.execute(USER_COMMAND);
		verify(command1).execute();
		verify(command2).execute();
	}

	@Test
	void return_all_objects_returned_by_executions_on_execute() {
		given(consoleCommandsFactory.commandsFor(USER_COMMAND)).willReturn(List.of(command1, command2));
		Object object2 = mock(Object.class);
		given(command1.execute()).willReturn(Optional.empty());
		given(command2.execute()).willReturn(Optional.of(object2));
		assertThat(commandExecutor.execute(USER_COMMAND)).isEqualTo(Optional.of(object2));
	}
}
