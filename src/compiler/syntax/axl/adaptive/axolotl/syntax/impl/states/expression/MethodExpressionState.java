package axl.adaptive.axolotl.syntax.impl.states.expression;

import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.lexical.TokenStream;
import axl.adaptive.axolotl.syntax.impl.DefaultSyntaxAnalyzer;
import axl.adaptive.axolotl.syntax.State;
import axl.adaptive.axolotl.syntax.ast.expression.MethodExpression;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MethodExpressionState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    @Nullable
    private final Consumer<MethodExpression> result;

    private final MethodExpression method;

    public MethodExpressionState(DefaultSyntaxAnalyzer analyzer, @Nullable Consumer<MethodExpression> result) {
        this.analyzer = analyzer;
        this.result = result;
        this.method = new MethodExpression(analyzer.getStream().next(), new ArrayList<>());
        analyzer.getStream().next();
    }

    @Override
    public void analyze() {
        final TokenStream stream = analyzer.getStream();
        if (!stream.hasNext())
            throw new RuntimeException();

        if (stream.get().getType() == TokenType.RIGHT_PARENT) {
            stream.next();
            analyzer.getStates().pop();
            if (result != null)
                result.accept(method);

            return;
        }

        if (method.getArguments().isEmpty()) {
            analyzer.getStates().push(new ExpressionState(analyzer, method.getArguments()::add));
            return;
        }

        if (stream.get().getType() == TokenType.COMMA) {
            stream.next();
            analyzer.getStates().push(new ExpressionState(analyzer, method.getArguments()::add));
            return;
        }

        throw new RuntimeException();
    }
}
