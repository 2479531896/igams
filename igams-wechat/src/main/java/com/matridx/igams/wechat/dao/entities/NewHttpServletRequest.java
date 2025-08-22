package com.matridx.igams.wechat.dao.entities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

public class NewHttpServletRequest  extends HttpServletRequestWrapper {

    private Map<String, String[]> parameterMap;

    public NewHttpServletRequest(HttpServletRequest request, Map<String, String[]> parameterMap) {
        super(request);
        this.parameterMap = parameterMap;
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(parameterMap);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.getOrDefault(name, new String[0]);
    }

}
