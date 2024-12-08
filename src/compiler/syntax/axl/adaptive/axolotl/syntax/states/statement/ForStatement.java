package axl.adaptive.axolotl.syntax.states.statement;

import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.utils.syntax.Node;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ForStatement implements Node {
    private Expression expression;

    private List<Node> body;

    @Override
    public String toString() {
        return "For {" +
                "expression=" + Arrays.stream(expression.toString().split(";")).map(Object::toString).collect(Collectors.joining(",")) +
                ",body=[" + body.stream().map(Object::toString).collect(Collectors.joining(",")) +
                "]}";
    }
}
