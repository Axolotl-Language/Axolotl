package axl.adaptive.axolotl.syntax;

import axl.utils.lexical.TokenStream;
import axl.adaptive.axolotl.syntax.ast.File;
import axl.utils.syntax.Node;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.adaptive.axolotl.syntax.states.declaration.FileState;
import axl.utils.syntax.State;
import axl.utils.syntax.SyntaxAnalyzer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
@Setter
public class DefaultSyntaxAnalyzer implements SyntaxAnalyzer<File> {

    private final Stack<Node> nodes = new Stack<>();

    private final Stack<Object> context = new Stack<>();

    private final Stack<State> states = new Stack<>();

    private final TokenStream stream;

    public DefaultSyntaxAnalyzer(TokenStream stream) {
        this.stream = stream;
    }

    @SneakyThrows
    @Override
    public @NonNull File analyze() {
        FileState fileState = new FileState(this);
        states.push(fileState);

        while(states.size() > 1 || stream.hasNext()) {
            states.peek().analyze();
        }

        if (stream.hasNext())
            throw new RuntimeException();

        return fileState.build();
    }

    private List<Expression> expr = new ArrayList<>();
}

