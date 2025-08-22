package com.matridx.igams.common.util.valuate.eval;


import com.matridx.igams.common.util.valuate.stream.TokenStream;

public interface StagePlanner {

    public EvaluationStage plan(TokenStream stream) throws Exception;
}
