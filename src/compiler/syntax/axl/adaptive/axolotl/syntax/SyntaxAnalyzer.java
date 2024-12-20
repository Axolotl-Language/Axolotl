package axl.adaptive.axolotl.syntax;

import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenType;
import axl.adaptive.axolotl.lexical.TokenStream;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SyntaxAnalyzer<T> {

    @NonNull T analyze();

    TokenStream getStream();

    default @NotNull Token eat(TokenType type) {
        if (!getStream().hasNext())
            throw new IllegalSyntaxException("Token not found", getStream());

        Token token = getStream().next();
        assert token != null;
        if (token.getType() != type)
            throw new IllegalSyntaxException("Unknown token", getStream().back());

        return token;
    }

    default @Nullable Token lowEat(TokenType type) {
        if (!getStream().hasNext())
            return null;

        Token token = getStream().get();
        if (token.getType() != type) {
            return null;
        }

        getStream().next();
        return token;
    }

    default boolean boolEat(TokenType type) {
        if (!getStream().hasNext())
            return false;

        Token token = getStream().get();
        if (token.getType() != type) {
            return false;
        }

        getStream().next();
        return true;
    }

    default boolean check(TokenType type) {
        if (!getStream().hasNext())
            return false;

        Token token = getStream().get();
        return token.getType() == type;
    }

    default void checkEnd() {
        if (!getStream().hasNext())
            throw new IllegalSyntaxException("Token not found", getStream().back());
    }
}
