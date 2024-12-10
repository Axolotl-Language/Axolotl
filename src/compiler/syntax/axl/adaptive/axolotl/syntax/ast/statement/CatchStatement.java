package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.syntax.Node;
import axl.adaptive.axolotl.syntax.ast.Type;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CatchStatement implements Node {

    @NotNull
    private Type type;

    @NotNull
    private Token name;

    @Override
    public String toString() {
        return "CatchStatement {" +
                "type=" + type +
                ",name=" + name +
                "}";
    }
}
