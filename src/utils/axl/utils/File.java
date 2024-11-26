package axl.utils;

import axl.adaptive.axolotl.lexical.DefaultTokenizer;
import axl.utils.lexical.Token;
import axl.utils.lexical.Tokenizer;
import axl.adaptive.axolotl.lexical.DefaultTokenStream;
import axl.utils.lexical.TokenStream;
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
        this.tokenizer = new DefaultTokenizer(this);
    }

    public TokenStream createTokenStream() {
        return new DefaultTokenStream(this, tokens, tokenizer);
    }

}
