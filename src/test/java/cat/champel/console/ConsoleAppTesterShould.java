package cat.champel.console;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsoleAppTesterShould {
	private static final String COMMAND = "COMMAND";
	private static final String RAW_OUTPUT = "RAW_OUTPUT";
	private static final String FORMATED_OUTPUT = "FORMATED_OUTPUT";
	
	@Mock CommandBuilder commandBuilder;
	@Mock Command command;
	@Mock Function<Object, String> formatter;
	
	private ConsoleAppTester consoleAppTester;
	
	@BeforeEach
	void setUp() throws Exception {
		consoleAppTester = new ConsoleAppTester();
		consoleAppTester.setup(new ConsoleApp(
				List.of(commandBuilder),
				Map.of(String.class, formatter)));
	}

	@Test
	void call_command_builder_on_tester_write() throws IOException {
		consoleAppTester.writeCommand(COMMAND + "\n");
		verify(commandBuilder, timeout(100)).build(COMMAND);
	}


	@Test
	void execute_generated_command_on_tester_write() throws IOException {
		given(commandBuilder.build(COMMAND)).willReturn(Optional.of(command));
		consoleAppTester.writeCommand(COMMAND + "\n");
		verify(command, timeout(100)).execute();
	}

	@Test
	void formats_execution_output_and_writes_it_in_console_on_tester_write() throws Exception {
		consoleAppTester.readOutput(" >"); // Prompt is always shown before a command can be written
		given(commandBuilder.build(COMMAND)).willReturn(Optional.of(command));
		given(command.execute()).willReturn(Optional.of(RAW_OUTPUT));
		given(formatter.apply(RAW_OUTPUT)).willReturn(FORMATED_OUTPUT);
		consoleAppTester.writeCommand(COMMAND + "\n");
		verify(formatter, timeout(100)).apply(RAW_OUTPUT);
		assertThat(consoleAppTester.readOutput(FORMATED_OUTPUT)).isEqualTo(FORMATED_OUTPUT);
	}

}
