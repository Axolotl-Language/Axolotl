package axl.adaptive.axolotl.syntax.ast.declaration;

import axl.utils.lexical.Token;
import axl.utils.syntax.Node;

public class StartFrame implements Node {

    private Token token;

    @Override
    public String toString() {
        return "StartFrame";
    }
}
