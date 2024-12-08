package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FrameEnd implements Node {

    @Override
    public String toString() {
        return "FrameEnd";
    }
}
