package axl.adaptive.axolotl.syntax.impl.states.statement;

import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.syntax.ast.statement.*;
import axl.adaptive.axolotl.syntax.impl.DefaultSyntaxAnalyzer;
import axl.adaptive.axolotl.syntax.Node;
import axl.adaptive.axolotl.syntax.State;
import axl.adaptive.axolotl.syntax.impl.StateController;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("DuplicatedCode")
public class BodyState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final List<Node> result;

    public BodyState(DefaultSyntaxAnalyzer analyzer, @NotNull List<Node> result) {
        this.analyzer = analyzer;
        this.result = result;
    }

    private boolean start = true;
    private boolean single = true;

    @Override
    public void analyze() {
        if (start) {
            start = false;
            result.add(new FrameStart());
            if (analyzer.boolEat(TokenType.LEFT_BRACE)) {
                single = false;
            } else {
                analyzer.getStates().pop();
                StateController.custom(analyzer, () -> {
                    analyzer.getStates().pop();
                    result.add(new FrameEnd());
                });
            }
        }

        if (analyzer.boolEat(TokenType.SEMI))
            return;

        analyzer.checkEnd();
        if (!single && analyzer.boolEat(TokenType.RIGHT_BRACE)) {
            analyzer.getStates().pop();
            result.add(new FrameEnd());
            return;
        }

        //  TODO add statements
        if (analyzer.boolEat(TokenType.RETURN)) {
            StateController.expression(analyzer, expression -> result.add(new ReturnStatement(expression)));
        } else if (analyzer.boolEat(TokenType.IF)) {
            ifStatement();
        } else if (analyzer.boolEat(TokenType.WHILE)) {
            whileStatement();
        } else if (analyzer.boolEat(TokenType.FOR)) {
            forStatement();
        } else if (analyzer.check(TokenType.LEFT_BRACE)) {
            StateController.custom(analyzer, () -> {
                analyzer.getStates().pop();
            });
            StateController.body(analyzer, result);
            StateController.custom(analyzer, () -> {
                analyzer.getStates().pop();
            });
        } else {
            StateController.expression(analyzer, result::add);
        }
    }

    private void ifStatement() {
        IfStatement ifStatement = new IfStatement();
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            if (analyzer.boolEat(TokenType.ELSE)) {
                StateController.custom(analyzer, () -> {
                    analyzer.getStates().pop();
                    result.add(new ElseStatement());
                });
                StateController.body(analyzer, result);
            }
        });
        StateController.body(analyzer, result);
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.add(ifStatement);
        });
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            StateController.expression(analyzer, ifStatement::setExpression);
        });
    }

    private void whileStatement() {
        WhileStatement whileStatement = new WhileStatement();

        StateController.body(analyzer, result);
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.add(whileStatement);
        });
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            StateController.expression(analyzer, whileStatement::setExpression);
        });
    }

    private void forStatement() {
        ForStatement forStatement = new ForStatement();

        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.add(forStatement);
        });
        StateController.body(analyzer, forStatement::setBody);
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            StateController.expression(analyzer, forStatement::setExpression);
        });
    }
}
