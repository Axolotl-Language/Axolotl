package axl.utils.lexical;

import axl.utils.File;
import lombok.NonNull;

import java.util.List;

public interface TokenStream {

    @NonNull
    File getFile();

    Token next();

    Token get();

    boolean hasNext();

    @NonNull Frame createFrame();

    void restoreFrame(@NonNull Frame frame);

    int peekLine(@NonNull Frame frame);

    int peekLastLine(@NonNull Frame frame);

    @NonNull TokenStream createSubStream(Frame start, Frame end);

    @NonNull List<Token> copy();

    void decrement();

    default TokenStream back() {
        decrement();
        return this;
    }
}
