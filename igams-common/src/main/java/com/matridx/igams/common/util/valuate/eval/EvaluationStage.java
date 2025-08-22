package com.matridx.igams.common.util.valuate.eval;

import com.matridx.igams.common.util.valuate.check.CombinedTypeCheck;
import com.matridx.igams.common.util.valuate.check.TypeCheck;
import com.matridx.igams.common.util.valuate.enums.ErrorFormat;
import com.matridx.igams.common.util.valuate.enums.OperatorSymbol;
import com.matridx.igams.common.util.valuate.operator.Operator;
import lombok.Builder;
import lombok.Data;

;

@Data
@Builder
public class EvaluationStage {
    private OperatorSymbol symbol;

    private EvaluationStage leftStage;

    private EvaluationStage rightStage;

    private Operator operator;

    private TypeCheck leftTypeCheck;

    private TypeCheck rightTypeCheck;

    private CombinedTypeCheck combinedTypeCheck;

    private ErrorFormat typeErrorFormat;


    public void swapWith(EvaluationStage other) {
        EvaluationStage temp = EvaluationStage.builder().build();
        temp.setToNonStage(other);
        other.setToNonStage(this);
        this.setToNonStage(temp);
    }

    public void setToNonStage(EvaluationStage other) {

        this.symbol = other.symbol;
        this.operator = other.operator;
        this.leftTypeCheck = other.leftTypeCheck;
        this.rightTypeCheck = other.rightTypeCheck;
        this.combinedTypeCheck = other.combinedTypeCheck;
        this.typeErrorFormat = other.typeErrorFormat;
    }

    public boolean isShortCircuitable() {

        switch (this.symbol) {
            case AND:
            case OR:
            case TERNARY_TRUE:
            case TERNARY_FALSE:
            case COALESCE:
                return true;
        }

        return false;
    }
}
