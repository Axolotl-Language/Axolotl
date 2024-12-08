package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.Node;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ForStatement implements Node {

    private Expression initializationExpression;

    private Expression conditionExpression;

    private Expression iterationExpression;

    @Override
    public String toString() {
        return "For {" +
                "initializationExpression=" + initializationExpression +
                ",conditionExpression=" + conditionExpression +
                ",iterationExpression=" + iterationExpression +
                "}";
    }
}
