package axl.adaptive.axolotl.lexical.impl;

import axl.adaptive.axolotl.lexical.TokenizerFrame;

public record TokenizerFrameImpl(int offset, int line, int column) implements TokenizerFrame {
}