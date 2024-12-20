package axl.adaptive.axolotl.syntax.impl.states.expression;

import axl.adaptive.axolotl.syntax.impl.DefaultSyntaxAnalyzer;
import axl.utils.File;
import axl.adaptive.axolotl.syntax.IllegalSyntaxException;
import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenGroup;
import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.lexical.Frame;
import axl.adaptive.axolotl.lexical.TokenStream;
import axl.adaptive.axolotl.syntax.ast.expression.Expression;
import axl.adaptive.axolotl.syntax.ast.expression.IdentifyExpression;
import axl.adaptive.axolotl.syntax.ast.expression.LiteralExpression;
import axl.adaptive.axolotl.syntax.Node;
import axl.adaptive.axolotl.syntax.State;
import axl.adaptive.axolotl.syntax.impl.StateController;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Getter
public class ExpressionState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    @Nullable
    private final Consumer<Expression> result;

    @Setter
    private Boolean lastExpression = false;

    private Boolean nextState = false;

    private final Frame start;

    private Frame last;

    private final int levelNodes;

    private final int levelContext;

    int parents = 0;

    int square = 0;

    public ExpressionState(DefaultSyntaxAnalyzer analyzer, @Nullable Consumer<Expression> result) {
        this.analyzer = analyzer;
        this.levelNodes = analyzer.getNodes().size();
        this.levelContext = analyzer.getContext().size();
        this.start = analyzer.getStream().createFrame();
        this.result = result;
    }

    @SneakyThrows
    @Override
    public void analyze() {
        final TokenStream stream = analyzer.getStream();

        main:
        while (stream.hasNext())  {
            if (stream.get().getType().getGroup() == TokenGroup.OPERATOR || stream.get().getType().getGroup() == TokenGroup.DELIMITER) {
                last = stream.createFrame();
                switch (stream.get().getType()) {
                    case SEMI:
//                        stream.next();
                    case COMMA, RIGHT_BRACE, LEFT_BRACE:
                        break main;
                }
                OperatorEntry entry = findOperator(stream.getFile(), stream.next());

                if (entry.getOperator().getOperator() == TokenType.RIGHT_PARENT) {
                    if (parents == 0) {
                        stream.restoreFrame(last);
                        break;
                    }

                    entry.accept(this);
                    continue;
                } else if (entry.getOperator().getOperator() == TokenType.LEFT_PARENT) {
                    parents++;
                } else if (entry.getOperator().getOperator() == TokenType.RIGHT_SQUARE) {
                    if (square == 0) {
                        stream.restoreFrame(last);
                        break;
                    }

                    entry.accept(this);
                    continue;
                } else if (entry.getOperator().getOperator() == TokenType.LEFT_SQUARE) {
                    if (!lastExpression) {
                        // TODO можно реализовать запись массива, например `[1, 3, 2]`
                        stream.restoreFrame(last);
                        throw new IllegalSyntaxException("Writing arrays like `[x, y, z,..]` is not allowed in this version", stream);
                    }

                    square++;
                }

                reduce(entry.getOperator().getPriority());
                lastExpression = false;
                if (entry.getType() == OperatorEntry.OperatorType.POSTFIX) {
                    if (stream.peekLastLine(last) == entry.getToken().getLine()) {
                        lastExpression = true;
                    } else {
                        stream.restoreFrame(last);
                        break;
                    }
                }
                pushContext(entry);
            } else if (findPrimary(stream)) {
                lastExpression = true;
            } else {
                nextState = true;
                break;
            }
        }

        if (nextState) {
            nextState = false;

            if (analyzer.getStates().peek() != this)
                return;
        }

        reduce();
        if (sizeExpressions() == 0) {
            stream.restoreFrame(start);
            throw new IllegalSyntaxException("Expression not found", stream);
        }

        if (sizeExpressions() != 1) {
            stream.restoreFrame(start);
            throw new IllegalSyntaxException("Invalid expression", stream);
        }

        analyzer.getStates().pop();
        if (result != null)
            result.accept(popExpression());
    }

    private boolean findPrimary(TokenStream stream) {
        if (getLastExpression()) {
            if (parents == 0 && square == 0)
                return false;

            throw new IllegalSyntaxException("Incorrect arrangement of expressions. Check the positions of the delimiters", stream);
        }

        if (stream.get().getType().getGroup() == TokenGroup.LITERAL) {
            pushExpression(new LiteralExpression(stream.next()));
        } else if (stream.get().getType() == TokenType.TRUE || stream.get().getType() == TokenType.FALSE) {
            pushExpression(new LiteralExpression(stream.next()));
        } else if (stream.get().getType() == TokenType.IDENTIFY) {
            Token name = stream.next();
            Frame frame = stream.createFrame();

            if (!stream.hasNext() || stream.get().getType() != TokenType.LEFT_PARENT) {
                stream.restoreFrame(frame);
                pushExpression(new IdentifyExpression(name));
                return true;
            }

            stream.decrement();
            StateController.methodCall(analyzer, methodExpression -> {
                pushExpression(methodExpression);
                setLastExpression(true);
            });
            return false;
        } else if (stream.get().getType() == TokenType.VAR) {
            StateController.var(analyzer, this::pushExpression);
            return false;
        } else if (stream.get().getType() == TokenType.VAL) {
            StateController.val(analyzer, this::pushExpression);
            return false;
        } else {
            return false;
        }

        return true;
    }

    private OperatorEntry findOperator(File file, Token token) {
        for (Operator operator : Operator.values()) {
            if (operator.getOperator() == token.getType()) {
                if (operator.getGenerator() == OperatorGenerator.UNARY) {
                    if (lastExpression)
                        return new OperatorEntry(operator, token, OperatorEntry.OperatorType.POSTFIX);
                    else
                        return new OperatorEntry(operator, token, OperatorEntry.OperatorType.PREFIX);
                }
                if (operator.getGenerator() == OperatorGenerator.PREFIX)
                    return new OperatorEntry(operator, token, OperatorEntry.OperatorType.PREFIX);

                return new OperatorEntry(operator, token, OperatorEntry.OperatorType.BINARY);
            }
        }

        throw new IllegalSyntaxException("Unknown operator: " + token.getType(), file, token);
    }

    private void reduce(int priority) {
        while (sizeContext() != 0 &&
                priority <= peekContext().getOperator().getPriority() &&
                peekContext().getOperator().getOperator() != TokenType.LEFT_PARENT &&
                peekContext().getOperator().getOperator() != TokenType.LEFT_SQUARE) {
            peekContext().accept(this);
        }
    }

    private void reduce() {
        while (sizeContext() != 0)
            peekContext().accept(this);
    }

    int sizeExpressions() {
        return analyzer.getNodes().size() - levelNodes;
    }

    void pushExpression(Expression expression) {
        this.analyzer.getNodes().push(expression);
    }

    Expression popExpression() {
        if (sizeExpressions() <= 0)
            throw new IllegalSyntaxException("Invalid expression", analyzer.getStream().back());

        Node node = this.analyzer.getNodes().pop();

        if (node instanceof Expression)
            return (Expression) node;

        throw new IllegalSyntaxException("Invalid expression", analyzer.getStream());
    }

    int sizeContext() {
        return analyzer.getContext().size() - levelContext;
    }

    void pushContext(OperatorEntry entry) {
        this.analyzer.getContext().push(entry);
    }

    OperatorEntry popContext() {
        if (sizeContext() <= 0)
            throw new IllegalSyntaxException("Invalid expression", analyzer.getStream());

        Object object = this.analyzer.getContext().pop();

        if (object instanceof OperatorEntry)
            return (OperatorEntry) object;

        throw new IllegalSyntaxException("Invalid expression", analyzer.getStream());
    }

    OperatorEntry peekContext() {
        if (sizeContext() <= 0)
            throw new IllegalSyntaxException("Invalid expression", analyzer.getStream());

        Object object = this.analyzer.getContext().peek();

        if (object instanceof OperatorEntry)
            return (OperatorEntry) object;

        throw new IllegalSyntaxException("Invalid expression", analyzer.getStream());
    }
}
