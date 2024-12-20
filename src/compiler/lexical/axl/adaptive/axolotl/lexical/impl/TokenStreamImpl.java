package axl.adaptive.axolotl.lexical.impl;

import axl.utils.File;
import axl.adaptive.axolotl.lexical.Frame;
import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.Tokenizer;
import axl.adaptive.axolotl.lexical.TokenStream;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TokenStreamImpl implements TokenStream {

    @Getter
    private final @NonNull File file;

    private final List<Token> tokens;

    private final Tokenizer tokenizer;

    private int iterator;

    private boolean processed;

    public TokenStreamImpl(@NonNull File file, @NonNull List<Token> tokens, Tokenizer tokenizer) {
        this.file = file;
        this.tokens = tokens;
        this.tokenizer = tokenizer;
        this.processed = tokenizer.isProcessed();
    }

    private TokenStreamImpl(@NonNull File file, @NonNull List<Token> tokens, boolean processed) {
        this.file = file;
        this.tokens = tokens;
        this.processed = processed;
        this.tokenizer = null;
    }

    @Override
    public @Nullable Token next() {
        if (iterator < tokens.size())
            return tokens.get(iterator++);

        if (processed)
            return null;

        tokenize();
        return next();
    }

    @Override
    public @Nullable Token get() {
        if (iterator < tokens.size())
            return tokens.get(iterator);

        if (processed)
            return null;

        tokenize();
        return get();
    }

    @Override
    public boolean hasNext() {
        return !this.processed || this.iterator < tokens.size();
    }


    @Override
    public @NonNull Frame createFrame() {
        return new FrameImpl(iterator);
    }

    @Override
    public void restoreFrame(@NonNull Frame frame) {
        this.iterator = frame.getTokenId();
    }

    @Override
    public int peekLine(@NonNull Frame frame) {
        return tokens.get(frame.getTokenId()).getLine();
    }

    @Override
    public int peekLastLine(@NonNull Frame frame) {
        if (frame.getTokenId() - 1 < 0)
            return 0;

        return tokens.get(frame.getTokenId() - 1).getLine();
    }

    @Override
    public @NonNull TokenStream createSubStream(@NonNull Frame start, @NonNull Frame end) {
        List<Token> tokens = this.tokens.subList(
                start.getTokenId(),
                end.getTokenId() - 1
        );
        return new TokenStreamImpl(file, tokens, true);
    }

    @Override
    public @NonNull List<Token> copy() {
        return new ArrayList<>(this.tokens);
    }

    @Override
    public void decrement() {
        if (iterator > 0)
            iterator--;
    }

    public void tokenize() {
        this.tokens.add(tokenizer.tokenize());
        processed = tokenizer.isProcessed();
    }
}
