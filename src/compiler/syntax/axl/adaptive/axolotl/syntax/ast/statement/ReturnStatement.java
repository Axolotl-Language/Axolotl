package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.adaptive.axolotl.syntax.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("ALL")
public class ReturnStatement implements Node {

    private final Expression expression;

    @Override
    public String toString() {
        return "Return {" +
                "expression=" + expression +
                "}";
    }
}
