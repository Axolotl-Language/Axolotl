package axl.adaptive.axolotl.syntax.impl.states.expression;

import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.syntax.ast.expression.ArrayExpression;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EmptyStackException;

@Getter
@AllArgsConstructor
enum OperatorGenerator {
    BINARY((analyzer, token, type) -> {
        analyzer.popContext();
        try {
            Expression right = analyzer.popExpression();
            Expression left = analyzer.popExpression();
            analyzer.pushExpression(new Expression.BinaryExpression(left, right, token));
            analyzer.setLastExpression(true);
        } catch (EmptyStackException e) {
            throw new IllegalStateException("Invalid expression: insufficient operands for binary operator " + type, e);
        }
    }),
    UNARY((analyzer, token, type) -> {
        analyzer.popContext();
        try {
            Expression value = analyzer.popExpression();
            if (type == OperatorEntry.OperatorType.POSTFIX)
                analyzer.pushExpression(new Expression.UnaryExpression.PostfixExpression(value, token));
            else
                analyzer.pushExpression(new Expression.UnaryExpression.PrefixExpression(value, token));
            analyzer.setLastExpression(true);
        } catch (EmptyStackException e) {
            throw new IllegalStateException("Invalid expression: insufficient operands for postfix unary operator " + type, e);
        }
    }),
    PREFIX((analyzer, token, type) -> {
        analyzer.popContext();
        try {
            Expression value = analyzer.popExpression();
            analyzer.pushExpression(new Expression.UnaryExpression.PrefixExpression(value, token));
            analyzer.setLastExpression(true);
        } catch (EmptyStackException e) {
            throw new IllegalStateException("Invalid expression: insufficient operands for prefix unary operator " + type, e);
        }
    }),
    PARENT((analyzer, token, type) -> {
        while (analyzer.sizeContext() != 0 && analyzer.peekContext().getOperator().getOperator() != TokenType.LEFT_PARENT)
            analyzer.peekContext().accept(analyzer);

        if (analyzer.sizeContext() == 0)
            throw new EmptyStackException();

        if (analyzer.popContext().getOperator().getOperator() != TokenType.LEFT_PARENT)
            throw new RuntimeException();

        if (analyzer.sizeExpressions() < 1)
            throw new RuntimeException();

        analyzer.parents--;
        analyzer.setLastExpression(true);
    }), // TODO output
    SQUARE((analyzer, token, type) -> {
        while (analyzer.sizeContext() != 0 && analyzer.peekContext().getOperator().getOperator() != TokenType.LEFT_SQUARE)
            analyzer.peekContext().accept(analyzer);

        if (analyzer.sizeContext() == 0)
            throw new EmptyStackException();

        if (analyzer.popContext().getOperator().getOperator() != TokenType.LEFT_SQUARE)
            throw new RuntimeException();

        Expression index = analyzer.popExpression();
        Expression root = analyzer.popExpression();
        analyzer.pushExpression(new ArrayExpression(
                root,
                index
        ));
        analyzer.square--;
        analyzer.setLastExpression(true);
    }), // TODO output
    EXCEPTION(((analyzer, token, type) -> {
        throw new RuntimeException();
    })); // TODO output

    private final Generator lambda;

    interface Generator {

        void accept(ExpressionState state, Token token, OperatorEntry.OperatorType type);

    }
}