package axl.adaptive.axolotl.syntax.ast.expression;

import axl.utils.lexical.Token;
import axl.adaptive.axolotl.syntax.ast.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class ValueDefineExpression extends Expression {

    @Nullable
    @Setter
    private Type type;

    @NotNull
    private final Token name;

    @Override
    public String toString() {
        return "ValueDefineExpression {" +
                "name=" + name.getType() +
                ",type=" + type +
                '}';
    }
}
