package axl.adaptive.axolotl.lexical;

import axl.utils.File;
import lombok.NonNull;

public interface Token {

    int getLine();

    int getColumn();

    @NonNull
    TokenType getType();

    @NonNull String getContent(File file);
}
