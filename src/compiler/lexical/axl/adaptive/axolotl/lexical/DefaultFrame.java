package axl.adaptive.axolotl.lexical;

import axl.utils.lexical.Frame;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class DefaultFrame implements Frame {

    private int tokenId;
}
