package cat.champel.console;

import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsoleCommandFactoryShould {
	private static final String A_USER_COMMAND = "a user commaand";
	@Mock CommandBuilder consoleCommandBuilder1;
	@Mock CommandBuilder consoleCommandBuilder2;
	@Mock CommandBuilder consoleCommandBuilder3;
	@Mock Command consoleCommand1;
	@Mock Command consoleCommand3;
	
	CommandsFactory commandFactory;

	@BeforeEach
	void setup() {
		commandFactory = new CommandsFactory(List.of(
				consoleCommandBuilder1,
				consoleCommandBuilder2,
				consoleCommandBuilder3));
	}

	@Test
	void call_build_on_all_builders_when_generating_commands_for_a_user_command() {
		commandFactory.commandsFor(A_USER_COMMAND);
		verify(consoleCommandBuilder1).build(A_USER_COMMAND);
		verify(consoleCommandBuilder2).build(A_USER_COMMAND);
		verify(consoleCommandBuilder3).build(A_USER_COMMAND);
	}
	
	@Test
	void return_an_empty_list_when_builders_do_not_create_commands() {
		given(consoleCommandBuilder1.build(A_USER_COMMAND)).willReturn(Optional.empty());
		given(consoleCommandBuilder2.build(A_USER_COMMAND)).willReturn(Optional.empty());
		given(consoleCommandBuilder3.build(A_USER_COMMAND)).willReturn(Optional.empty());
		assertThat(commandFactory.commandsFor(A_USER_COMMAND)).isEqualTo(EMPTY_LIST);
	}

	@Test
	void return_a_list_of_all_geneerated_commands() {
		given(consoleCommandBuilder1.build(any())).willReturn(Optional.of(consoleCommand1));
		given(consoleCommandBuilder2.build(any())).willReturn(Optional.empty());
		given(consoleCommandBuilder3.build(any())).willReturn(Optional.of(consoleCommand3));
		assertThat(commandFactory.commandsFor(A_USER_COMMAND)).isEqualTo(List.of(consoleCommand1, consoleCommand3));
	}
}
