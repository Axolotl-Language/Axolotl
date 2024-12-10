package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.Node;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TryFrameEnd implements Node {

    @Override
    public String toString() {
        return "TryFrameEnd";
    }
}
