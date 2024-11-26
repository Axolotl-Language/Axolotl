package axl.adaptive.axolotl.syntax.states.expression;

import axl.utils.lexical.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public class OperatorEntry {

    private final Operator operator;

    private final Token token;

    private final OperatorType type;

    public void accept(ExpressionState state) {
        operator.getGenerator().getLambda().accept(state, token, type);
    }

    enum OperatorType {
        BINARY,
        PREFIX,
        POSTFIX
    }
}