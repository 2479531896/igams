package com.matridx.igams.common.util.valuate.operator;

import com.matridx.igams.common.util.valuate.domain.MapParameters;
import com.matridx.igams.common.util.valuate.result.EvaluationResult;
import lombok.Builder;

@Builder
public class EvaluationParameterOperator implements Operator {

    private String parameterName;

    public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
        return EvaluationResult.builder().result(parameters.get(parameterName)).leftStage(leftStage).rightStage(rightStage).build();
    }
}
