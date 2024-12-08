package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.adaptive.axolotl.syntax.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Setter
@Getter
public class WhileStatement implements Node {

    private Expression expression;

    private List<Node> body;

    @Override
    public String toString() {
        return "While {" +
                "expression=" + expression +
                ",body=[" + body.stream().map(Object::toString).collect(Collectors.joining(",")) +
                "]}";
    }
}
