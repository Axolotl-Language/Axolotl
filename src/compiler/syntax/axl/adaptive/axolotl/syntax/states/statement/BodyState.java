package axl.adaptive.axolotl.syntax.states.statement;

import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.syntax.DefaultSyntaxAnalyzer;
import axl.adaptive.axolotl.syntax.ast.declaration.EndFrame;
import axl.adaptive.axolotl.syntax.ast.declaration.StartFrame;
import axl.utils.syntax.Node;
import axl.utils.syntax.State;
import axl.adaptive.axolotl.syntax.StateController;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
            if (analyzer.boolEat(TokenType.LEFT_BRACE)) {
                single = false;
            } else {
                analyzer.getStates().pop();
            }
        }

        if (analyzer.boolEat(TokenType.SEMI))
            return;

        analyzer.checkEnd();
        if (!single && analyzer.boolEat(TokenType.RIGHT_BRACE)) {
            analyzer.getStates().pop();
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
                result.add(new EndFrame());
            });
            StateController.body(analyzer, result);
            StateController.custom(analyzer, () -> {
                analyzer.getStates().pop();
                result.add(new StartFrame());
            });
        } else {
            StateController.expression(analyzer, result::add);
        }
    }

    private void ifStatement() {
        IfStatement ifStatement = new IfStatement();

        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.add(ifStatement);
        });
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            if (analyzer.boolEat(TokenType.ELSE)) {
                StateController.body(analyzer, ifStatement::setElseBody);
            } else {
                ifStatement.setElseBody(new ArrayList<>());
            }
        });
        StateController.body(analyzer, ifStatement::setBody);
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            StateController.expression(analyzer, ifStatement::setExpression);
        });
    }

    private void whileStatement() {
        WhileStatement whileStatement = new WhileStatement();

        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.add(whileStatement);
        });
        StateController.body(analyzer, whileStatement::setBody);
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
