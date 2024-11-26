package axl.adaptive.axolotl.syntax.states.statement;

import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.utils.syntax.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class IfStatement implements Node {

    private Expression expression;

    private List<Node> body;

    private List<Node> elseBody;

    @Override
    public String toString() {
        return "If {" +
                "expression=" + expression +
                ",body=[" + body.stream().map(Object::toString).collect(Collectors.joining(",")) +
                "],else=[" + elseBody.stream().map(Object::toString).collect(Collectors.joining(",")) +
                "]}";
    }
}
