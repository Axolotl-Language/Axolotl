package axl.adaptive.axolotl.lexical;

import axl.utils.lexical.TokenizerFrame;

public record DefaultTokenizerFrame(int offset, int line, int column) implements TokenizerFrame {
}