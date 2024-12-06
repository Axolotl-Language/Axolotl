package axl.adaptive.axolotl.syntax.ast.statement;

import axl.adaptive.axolotl.syntax.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FrameStart implements Node {

    private FrameEnd end;

    @Override
    public String toString() {
        return "FrameStart";
    }
}
