package axl.adaptive.axolotl.lexical;

import axl.utils.File;
import axl.utils.lexical.Token;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class DefaultToken implements Token {

    private final TokenType type;

    int offset;

    int length;

    int line;

    int column;

    DefaultToken(TokenType type) {
        this.type = type;
    }

    @Override
    public @NotNull String getContent(File file) {
        return file
                .getContent()
                .substring(
                        this.offset,
                        this.offset + this.length
                );
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
