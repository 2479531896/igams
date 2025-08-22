package com.matridx.igams.common.util.valuate.operator;

import com.matridx.igams.common.util.valuate.domain.MapParameters;
import com.matridx.igams.common.util.valuate.result.EvaluationResult;
import lombok.Builder;

@Builder
public class EvaluationLiteralOperator implements Operator {

    private Object literal;

    public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
        return EvaluationResult.builder().result(literal).leftStage(leftStage).rightStage(rightStage).build();
    }
}
