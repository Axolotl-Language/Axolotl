package axl.adaptive.axolotl.syntax.ast.expression;

import axl.adaptive.axolotl.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class MethodExpression extends Expression {

    @NotNull
    private final Token name;

    @NotNull
    private final List<Expression> arguments;

    @Override
    public String toString() {
        return "MethodExpression {" +
                "name=" + name.getType() +
                ",args=" + Arrays.toString(arguments.stream().map(Object::toString).toArray()) +
                "}";
    }
}
