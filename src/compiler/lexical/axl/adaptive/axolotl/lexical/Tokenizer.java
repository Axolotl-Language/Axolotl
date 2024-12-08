package axl.adaptive.axolotl.lexical;

import axl.utils.File;
import lombok.NonNull;

public interface Tokenizer {

    @NonNull File getFile();

    @NonNull
    Token tokenize();

    boolean isProcessed();
}
