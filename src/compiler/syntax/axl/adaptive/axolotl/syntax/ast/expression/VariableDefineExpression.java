package axl.adaptive.axolotl.syntax.ast.expression;

import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.syntax.ast.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class VariableDefineExpression extends Expression {

    @Setter
    @Nullable
    private Type type;

    @NotNull
    private final Token name;

    @Override
    public String toString() {
        return "VariableDefineExpression {" +
                "name=" + name.getType() +
                ",type=" + type +
                '}';
    }
}
