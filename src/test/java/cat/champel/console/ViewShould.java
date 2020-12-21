package cat.champel.console;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
public class ViewShould {
	
	public static final Integer OBJECT1 = Integer.valueOf(1);
	private static final String OBJECT1_TO_STRING = OBJECT1.toString();
	public static final Float OBJECT2 = Float.valueOf(2);
	private static final String OBJECT2_FORMATTED = "Success";
	
	@Mock Function<Object, String> formatter;
	
	View view;
	
	@BeforeEach
	void setup() {
		view = new View(Map.of(Float.class, formatter));
	}

	@Test
	void format_object_using_class_formatter() {
		given(formatter.apply(OBJECT2)).willReturn(OBJECT2_FORMATTED);
		assertThat(view.display(OBJECT2)).isEqualTo(Optional.of(OBJECT2_FORMATTED));
	}

	@Test
	void format_object_with_toString_as_fallback() {
		assertThat(view.display(OBJECT1)).isEqualTo(Optional.of(OBJECT1_TO_STRING));
	}

	@Test
	void return_formated_elements_separated_with_new_line_if_object_is__list() {
		given(formatter.apply(OBJECT2)).willReturn(OBJECT2_FORMATTED);
		assertThat(view.display(List.of(OBJECT1, OBJECT2))).isEqualTo(Optional.of(OBJECT1_TO_STRING + "\n" + OBJECT2_FORMATTED));
	}

	@Test
	void return_an_empty_if_formatted_object_is_an_empty_list() {
		assertThat(view.display(List.of())).isEqualTo(Optional.empty());
	}
}
