package com.matridx.igams.common.util.valuate.eval;

import com.matridx.igams.common.util.valuate.check.CombinedTypeCheck;
import com.matridx.igams.common.util.valuate.check.TypeCheck;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationTypeChecks {
    private TypeCheck left;
    private TypeCheck right;
    private CombinedTypeCheck combined;
}
