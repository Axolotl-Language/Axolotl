package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.Node;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TryFrameStart implements Node {

    // semantic
    private TryFrameEnd end;

    @Override
    public String toString() {
        return "TryFrameStart";
    }
}
