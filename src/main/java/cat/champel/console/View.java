package cat.champel.console;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

class View {
	private Map<Class<?>, Function<Object, String>> formatters;

	View(Map<Class<?>, Function<Object, String>> formatters) {
		this.formatters = formatters;
		
	}

	public Optional<String> display(Object object) {
		List<Object> elements = (object instanceof List) ? (List<Object>) object : List.of(object);
		if (elements.isEmpty()) return Optional.empty(); 
		return Optional.of(elements.stream()
				.map((Object element) ->
					Optional.ofNullable(formatters.get(element.getClass()))
						.map(formatter -> formatter.apply(element))
						.orElseGet(() -> element.toString()))
				.collect(joining("\n")));
	}
}
