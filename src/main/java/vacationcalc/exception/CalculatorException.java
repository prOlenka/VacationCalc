package vacationcalc.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculatorException extends RuntimeException {

    public CalculatorException(String message) {
        super(message);
    }
}
