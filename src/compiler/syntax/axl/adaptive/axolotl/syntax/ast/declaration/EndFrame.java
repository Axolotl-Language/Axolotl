package axl.adaptive.axolotl.syntax.ast.declaration;

import axl.utils.lexical.Token;
import axl.utils.syntax.Node;

public class EndFrame implements Node {

    private Token token;

    @Override
    public String toString() {
        return "EndFrame";
    }
}
