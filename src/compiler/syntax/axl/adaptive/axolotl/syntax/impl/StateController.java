package axl.adaptive.axolotl.syntax.impl;

import axl.adaptive.axolotl.syntax.Node;
import axl.adaptive.axolotl.syntax.State;
import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.lexical.TokenStream;
import axl.adaptive.axolotl.syntax.impl.states.declaration.FunctionState;
import axl.adaptive.axolotl.syntax.impl.states.expression.ExpressionState;
import axl.adaptive.axolotl.syntax.impl.states.expression.MethodExpressionState;
import axl.adaptive.axolotl.syntax.impl.states.statement.BodyState;
import axl.adaptive.axolotl.syntax.IllegalSyntaxException;
import axl.adaptive.axolotl.syntax.impl.states.StateUtils;
import axl.adaptive.axolotl.syntax.ast.File;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.adaptive.axolotl.syntax.ast.expression.MethodExpression;
import axl.adaptive.axolotl.syntax.ast.expression.ValueDefineExpression;
import axl.adaptive.axolotl.syntax.ast.expression.VariableDefineExpression;
import axl.adaptive.axolotl.syntax.ast.Type;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class StateController {

    public static void type(DefaultSyntaxAnalyzer analyzer, Consumer<Type> result) {
        analyzer.getStates().push(() -> {
            List<Token> tokens = StateUtils.getLocation(analyzer);
            result.accept(new Type(tokens));
            analyzer.getStates().pop();
        });
    }

    public static void expression(DefaultSyntaxAnalyzer analyzer, Consumer<Expression> result) {
        analyzer.getStates().push(new ExpressionState(analyzer, result));
    }

    public static void methodCall(DefaultSyntaxAnalyzer analyzer, Consumer<MethodExpression> result) {
        analyzer.getStates().push(new MethodExpressionState(analyzer, result));
    }

    public static void var(DefaultSyntaxAnalyzer analyzer, Consumer<VariableDefineExpression> result) {
        analyzer.getStates().push(() -> {
            TokenStream stream = analyzer.getStream();

            stream.next();
            if (!stream.hasNext())
                throw new IllegalSyntaxException("Invalid variable declaration entry", stream);

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new IllegalSyntaxException("Invalid variable declaration entry", stream);

            VariableDefineExpression variableDefineExpression = new VariableDefineExpression(null, token);
            analyzer.getStates().pop();
            result.accept(variableDefineExpression);

            if (stream.hasNext() && stream.get().getType() == TokenType.COLON) {
                stream.next();
                if (!stream.hasNext())
                    throw new IllegalSyntaxException("Invalid type", stream);

                StateController.type(analyzer, variableDefineExpression::setType);
            }
        });
    }

    public static void val(DefaultSyntaxAnalyzer analyzer, Consumer<ValueDefineExpression> result) {
        analyzer.getStates().push(() -> {
            TokenStream stream = analyzer.getStream();

            stream.next();
            if (!stream.hasNext())
                throw new IllegalSyntaxException("Invalid value declaration entry", stream);

            Token token = stream.next();
            if (token.getType() != TokenType.IDENTIFY)
                throw new IllegalSyntaxException("Invalid value declaration entry", stream);

            ValueDefineExpression valueDefineExpression = new ValueDefineExpression(null, token);
            result.accept(valueDefineExpression);

            analyzer.getStates().pop();
            if (stream.hasNext() && stream.get().getType() == TokenType.COLON) {
                stream.next();
                if (!stream.hasNext())
                    throw new IllegalSyntaxException("Invalid type", stream);

                StateController.type(analyzer, valueDefineExpression::setType);
            }
        });
    }

    public static void function(DefaultSyntaxAnalyzer analyzer, Consumer<File.Function> result) {
        analyzer.getStates().push(new FunctionState(analyzer, result));
    }

    public static void custom(DefaultSyntaxAnalyzer analyzer, State state) {
        analyzer.getStates().push(state);
    }

    public static void body(DefaultSyntaxAnalyzer analyzer, @NotNull List<Node> body) {
        analyzer.getStates().push(new BodyState(analyzer, body));
    }
}
