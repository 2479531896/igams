package com.matridx.igams.common.util.valuate.result;

import com.matridx.igams.common.util.valuate.domain.ExpressionToken;
import com.matridx.igams.common.util.valuate.enums.TokenKind;
import lombok.Data;

@Data
public class ReadTokenResult {
    public ExpressionToken token;
    public TokenKind kind;

    public ReadTokenResult(TokenKind kind, Object tokenValue) {
        this.token = new ExpressionToken(kind, tokenValue);
        this.kind = kind;
    }
}
