package com.matridx.igams.common.util.valuate.operator;


import com.matridx.igams.common.util.valuate.domain.MapParameters;
import com.matridx.igams.common.util.valuate.result.EvaluationResult;

public interface Operator {

    EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception;
}
