package axl.adaptive.axolotl.syntax.impl.states.declaration;

import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.lexical.TokenStream;
import axl.adaptive.axolotl.syntax.impl.DefaultSyntaxAnalyzer;
import axl.adaptive.axolotl.syntax.ast.Argument;
import axl.adaptive.axolotl.syntax.ast.File;
import axl.adaptive.axolotl.syntax.State;
import axl.adaptive.axolotl.syntax.impl.StateController;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class FunctionState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final Consumer<File.Function> result;

    public FunctionState(DefaultSyntaxAnalyzer analyzer, @NotNull Consumer<File.Function> result) {
        // TODO annotations
        this.analyzer = analyzer;
        this.result = result;
    }

    @Override
    public void analyze() {
        analyzer.getStates().pop();
        TokenStream stream = analyzer.getStream();
        stream.next();

        File.Function function = new File.Function();

        function.setName(analyzer.eat(TokenType.IDENTIFY));

        StateController.body(analyzer, function.getBody());

        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            if (!analyzer.getStream().hasNext() ||
                    analyzer.check(TokenType.FN) ||
                    analyzer.check(TokenType.AT_SYMBOL) ||
                    analyzer.check(TokenType.VAR) ||
                    analyzer.check(TokenType.VAL) ||
                    analyzer.check(TokenType.STRUCT) ||
                    analyzer.check(TokenType.REF) ||
                    analyzer.check(TokenType.ON) ||
                    analyzer.check(TokenType.EVENT)
            ) analyzer.getStates().pop();
        });
        // return type
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            if (analyzer.boolEat(TokenType.IMPLICATION))
                StateController.type(analyzer, function::setReturnType);
        });
        argumentsState(function.getArguments());
        StateController.custom(analyzer, () -> {
            analyzer.getStates().pop();
            result.accept(function);
        });
    }

    private void argumentsState(List<Argument> arguments) {
        StateController.custom(analyzer, new State() {
            private boolean start = true;

            @Override
            public void analyze() {
                if (start) {
                    start = false;
                    analyzer.eat(TokenType.LEFT_PARENT);
                }

                Token name = analyzer.lowEat(TokenType.IDENTIFY);
                if (name != null) {
                    analyzer.eat(TokenType.COLON);
                    StateController.type(analyzer, type -> arguments.add(new Argument(type, name)));
                    return;
                }

                if (analyzer.boolEat(TokenType.COMMA))
                    return;

                analyzer.eat(TokenType.RIGHT_PARENT);
                analyzer.getStates().pop();
            }
        });
    }
}
