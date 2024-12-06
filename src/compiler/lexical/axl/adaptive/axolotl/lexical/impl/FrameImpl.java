package axl.adaptive.axolotl.lexical.impl;

import axl.adaptive.axolotl.lexical.Frame;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class FrameImpl implements Frame {

    private int tokenId;
}
