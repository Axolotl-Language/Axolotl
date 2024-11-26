package axl.adaptive.axolotl.syntax.ast.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class ArrayExpression extends Expression {

     @NotNull
     private final Expression root;

     @NotNull
     private final Expression index;

    @Override
    public String toString() {
        return "ArrayExpression";
    }
}
