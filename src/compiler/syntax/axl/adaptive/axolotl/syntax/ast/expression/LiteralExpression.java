package axl.adaptive.axolotl.syntax.ast.expression;

import axl.adaptive.axolotl.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class LiteralExpression extends Expression {

    @NotNull
    private final Token value;

    @Override
    public String toString() {
        return "LiteralExpression {" +
                "value=" + value.getType() +
                '}';
    }
}
