package axl.adaptive.axolotl.syntax.ast.expression;

import axl.utils.lexical.Token;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public  class IdentifyExpression extends Expression {

    @NotNull
    private final Token value;

    @Override
    public String toString() {
        return "IdentifyExpression {" +
                "value=" + value.getType() +
                '}';
    }
}
