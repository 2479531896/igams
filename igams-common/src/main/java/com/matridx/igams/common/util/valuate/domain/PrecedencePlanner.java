package com.matridx.igams.common.util.valuate.domain;

import com.matridx.igams.common.util.valuate.enums.ErrorFormat;
import com.matridx.igams.common.util.valuate.enums.OperatorSymbol;
import com.matridx.igams.common.util.valuate.enums.TokenKind;
import com.matridx.igams.common.util.valuate.eval.StagePlanner;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PrecedencePlanner {
    private Map<String, OperatorSymbol> validSymbols;
    private TokenKind[] validKinds;
    private ErrorFormat typeErrorFormat;

    private StagePlanner next;
    private StagePlanner nextRight;
}
