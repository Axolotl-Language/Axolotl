package axl.utils.lexical;

import axl.adaptive.axolotl.lexical.TokenType;
import axl.utils.File;
import lombok.NonNull;

public interface Token {

    int getLine();

    int getColumn();

    @NonNull
    TokenType getType();

    @NonNull String getContent(File file);
}
