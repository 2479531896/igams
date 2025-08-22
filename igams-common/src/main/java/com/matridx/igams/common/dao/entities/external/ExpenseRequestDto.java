package com.matridx.igams.common.dao.entities.external;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} ExpenseRequestDto
 * {@code @description} TODO
 * {@code @date} 11:09 2023/4/23
 **/
public class ExpenseRequestDto {
    //需要导入的对应员工的工号
    private String employeeId;
    //对应的费用list
    private List<Expense> expenseList;
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }
}
