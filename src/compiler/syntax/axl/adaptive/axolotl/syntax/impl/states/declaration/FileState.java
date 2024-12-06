package axl.adaptive.axolotl.syntax.impl.states.declaration;

import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.lexical.Frame;
import axl.adaptive.axolotl.lexical.TokenStream;
import axl.adaptive.axolotl.syntax.impl.DefaultSyntaxAnalyzer;
import axl.adaptive.axolotl.syntax.IllegalSyntaxException;
import axl.adaptive.axolotl.syntax.impl.states.StateUtils;
import axl.adaptive.axolotl.syntax.ast.File;
import axl.adaptive.axolotl.syntax.State;
import axl.adaptive.axolotl.syntax.impl.StateController;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class FileState implements State {

    private final DefaultSyntaxAnalyzer analyzer;

    private final TokenStream stream;

    private final File file;

    public FileState(DefaultSyntaxAnalyzer analyzer) {
        this.analyzer = analyzer;
        this.stream = analyzer.getStream();

        this.file = new File();
        this.file.setFile(stream.getFile());
    }

    @SneakyThrows
    @Override
    public void analyze() {
        if (file.getLocation() == null)
            setup();

        // TODO annotations

        Token token = stream.get();
        switch (token.getType()) {
            case FN -> {
                StateController.function(analyzer, file.getFunctions()::add);
                return;
            }
            case REF -> {
                // TODO classes
            }
            case EVENT -> {
                // TODO events
            }
            case ON -> {
                // TODO listeners
            }
            case STRUCT -> {
                // TODO structures
            }
        }

        if (stream.hasNext())
            throw new IllegalSyntaxException("Undefined token", stream);
    }

    private void setup() {
        this.analyzer.getNodes().push(this.file);
        if (stream.get().getType() == TokenType.PACKAGE) {
            Frame frame = stream.createFrame();
            stream.next();
            file.setLocation(StateUtils.getLocation(analyzer));
            if (stream.hasNext() && stream.get().getType() == TokenType.DOT) {
                stream.restoreFrame(frame);
                throw new IllegalSyntaxException("Invalid location", stream);
            }
        } else {
            file.setLocation(new ArrayList<>());
        }

        while (stream.hasNext() && stream.get().getType() == TokenType.IMPORT) {
            stream.next();
            List<Token> location = StateUtils.getLocation(analyzer);
            boolean all = false;

            if (stream.hasNext() && stream.get().getType() == TokenType.DOT) {
                stream.next();

                if (stream.hasNext() && stream.get().getType() != TokenType.MULTIPLY)
                    throw new IllegalSyntaxException("Invalid location", stream);

                stream.next();
                all = true;
            }

            this.file.getImports().add(new File.Import(location, all));
        }
    }

    public File build() {
        return this.file;
    }
}
