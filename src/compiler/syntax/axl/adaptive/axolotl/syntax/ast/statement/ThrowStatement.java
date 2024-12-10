package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.Node;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("ALL")
public class ThrowStatement implements Node {

    private final Expression expression;

    @Override
    public String toString() {
        return "Throw {" +
                "expression=" + expression +
                "}";
    }
}
