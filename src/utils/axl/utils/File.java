package axl.utils;

import axl.adaptive.axolotl.lexical.impl.TokenizerImpl;
import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.Tokenizer;
import axl.adaptive.axolotl.lexical.impl.TokenStreamImpl;
import axl.adaptive.axolotl.lexical.TokenStream;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// TODO
public class File {

    @Getter
    private final String filename;

    @Getter
    private final String content;

    private final List<Token> tokens;

    private final Tokenizer tokenizer;

    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
        this.tokens = new ArrayList<>();
        this.tokenizer = new TokenizerImpl(this);
    }

    public TokenStream createTokenStream() {
        return new TokenStreamImpl(this, tokens, tokenizer);
    }

}
