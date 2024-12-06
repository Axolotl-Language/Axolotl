package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.adaptive.axolotl.syntax.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class IfStatement implements Node {

    private Expression expression;

    @Override
    public String toString() {
        return "If {" +
                "expression=" + expression +
                "}";
    }
}
