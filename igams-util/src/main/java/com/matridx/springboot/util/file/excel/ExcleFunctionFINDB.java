package com.matridx.springboot.util.file.excel;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

public class ExcleFunctionFINDB implements FreeRefFunction
{

    @Override
    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 2 || args.length != 3){
            return ErrorEval.VALUE_INVALID;
        }
        try {
            //要查找的文本
            ValueEval find_text = OperandResolver.getSingleValue( args[0],
                    ec.getRowIndex(),
                    ec.getColumnIndex() ) ;
            //含要查找文本的文本
            ValueEval within_text = OperandResolver.getSingleValue( args[1],
                    ec.getRowIndex(),
                    ec.getColumnIndex() ) ;
            String find_text_str = OperandResolver.coerceValueToString(find_text);
            String within_text_str = OperandResolver.coerceValueToString(within_text);
            int find_index = -1;
            //含要查找文本的文本
            if (args.length != 2){
                ValueEval start_num = OperandResolver.getSingleValue( args[2],
                        ec.getRowIndex(),
                        ec.getColumnIndex() ) ;
                int start_num_int = OperandResolver.coerceValueToInt(start_num);
                if (start_num_int > within_text_str.length()){
                    return ErrorEval.VALUE_INVALID;
                }
                find_index = find_text_str.indexOf(within_text_str,start_num_int-1);
            }else {
                find_index = find_text_str.indexOf(within_text_str);
            }
            if (find_index < 0){
                return ErrorEval.VALUE_INVALID;
            }
            return new NumberEval( find_index+1 );

        } catch (EvaluationException e) {
            return ErrorEval.VALUE_INVALID;
        }
    }

}
