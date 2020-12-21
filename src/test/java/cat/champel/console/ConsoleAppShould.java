package cat.champel.console;


import static cat.champel.console.ConsoleApp.BYE_MESSAGE;
import static cat.champel.console.ConsoleApp.EXIT_COMMAND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ConsoleAppShould {
	private static final String DUMMY_MESSAGE = "A message";
	private final static String DUMMY_POST_COMMAND = "A Post command"; 
	private final static String DUMMY_READ_COMMAND = "A Read read command";
	private final static Optional<Object> DUMMY_NO_OUTPUT = Optional.empty();
	private static Object DUMMY_OBJECT = new Object();
	private final static Optional<Object> DUMMY_WITH_OUTPUT = Optional.of(DUMMY_OBJECT);

	@Mock private CommandExecutor commandExecutor;
	@Mock private View view;
	@Mock private Console console;
	private ConsoleApp social;

	@BeforeEach
	void setup() {
		social = new ConsoleApp(console, commandExecutor, view);
	}

	@Test
	void exit_when_exit_is_received() {
		given(console.readLine()).willReturn(EXIT_COMMAND);
		social.run();
		verify(console).write(BYE_MESSAGE);
	}

	@Test
	void execute_user_commands_until_user_exits() {
		given(console.readLine()).willReturn(DUMMY_POST_COMMAND, DUMMY_READ_COMMAND, EXIT_COMMAND);
		given(commandExecutor.execute(any())).willReturn(DUMMY_NO_OUTPUT, DUMMY_NO_OUTPUT);
		social.run();
		InOrder inOrder = Mockito.inOrder(commandExecutor);
		inOrder.verify(commandExecutor).execute(DUMMY_POST_COMMAND);
		inOrder.verify(commandExecutor).execute(DUMMY_READ_COMMAND);
	}

	@Test
	void write_commands_output_in_console() {
		given(console.readLine()).willReturn(DUMMY_POST_COMMAND, DUMMY_READ_COMMAND, EXIT_COMMAND);
		given(commandExecutor.execute(any())).willReturn(DUMMY_NO_OUTPUT, DUMMY_WITH_OUTPUT);
		given(view.display(DUMMY_OBJECT)).willReturn(Optional.of(DUMMY_MESSAGE));
		social.run();
		InOrder inOrder = Mockito.inOrder(console);
		inOrder.verify(console).write(DUMMY_MESSAGE);
		inOrder.verify(console).write(BYE_MESSAGE);
	}
}
