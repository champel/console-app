Console application buildeer
=================================

Simple console application builder with support for testing. Created in the context of the [social network kata](https://github.com/champel/social-network-kata)

### Getting started

To use the console, you must provide a list of command builders and a map of formatters.

```java
	import java.util.List;
	import java.util.Map;
	
	import cat.champel.console.ConsoleApp;
	
	public class ExampleConsoleApp extends ConsoleApp {
		public ExampleConsoleApp() {
			super(
				List.of(
					new Action1CommandBuilder(),
					new Action2CommandBuilder()
				),
				Map.of(
					OutputExample.class, new OutputFormatter()
				)
			);
		}
		
		public static void main(String[] args) {
			new SocialConsoleApp().run();
		}
	}
```

The <code>CommandBuilder</code> parses the input introduced by the user and optionally returns a <code>Command</code>. The command has an execute method that will optionally return an output.

```java
	import java.util.Optional;
	
	import cat.champel.console.Command;
	import cat.champel.console.CommandBuilder;
	
	public class Action1CommandBuilder implements CommandBuilder {
	
	
		public static final String ARROW = " -> ";
	
		@Override
		public Optional<Command> build(String userCommand) {
			if (!userCommand.contains(ARROW)) return Optional.empty();
			String[] components = userCommand.split(ARROW);
			String userName = components[0];
			String message = components[1];
			return Optional.of(() -> Optional.of(userName + " posted: " + message));
		}
	}
```

### Testing

The library also incorporates a <code>ConsoleAppTester</code> that includes <code>setup</code>, <code>writeCommand</code> and <code>readOutput</code> to facilitate the testing of a console.

Here an example of glue code of a cucumber test using the tester:

```java
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import cat.champel.console.ConsoleAppTester;
import io.cucumber.java8.En;

public class ConsoleAppE2EATStepsDef extends ConsoleAppTester implements En {
	public ConsoleAppE2EATStepsDef() {
		Before(() -> setup(new SocialConsoleApp()));
		Given("in:  > {string}", (String command) -> processInput(command));
		Then( "out: {string}", (String chars) -> assertOutput(chars.replace("\\n","\n")));
	}

	private void processInput(String command) throws Exception, IOException {
		assertOutput(PROMPT);
		writeCommand(command);
	}
	
	private void assertOutput(String expected) throws Exception {
		assertThat(readOutput(expected)).isEqualTo(expected);
	}
}
```