package com.gperedu.meframework.webmvc.servlet;

import com.gperedu.meframework.annotation.GPRequestMapping;
import com.gperedu.meframework.annotation.GPRequetParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rogan on 2019/4/21.
 */
public class GPHandlerAdapter {


    boolean supports(Object handler){
//        return handler.getClass().isAnnotationPresent(GPRequestMapping.class);
        return handler instanceof  GPHandlerMapping;
    }

    GPModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        GPHandlerMapping handlerMapping = (GPHandlerMapping) handler;
        Map<String,Integer> paramMapping = new HashMap<>();
        Annotation[][] parameterAnnotations = handlerMapping.getMethod().getParameterAnnotations();

        for(int i = 0 ; i< parameterAnnotations.length; i++){
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for(Annotation annotation : parameterAnnotation)
            {
                if(GPRequetParam.class.isAssignableFrom(annotation.getClass()))
                {
                    String paramName = ((GPRequetParam) annotation).value();
                    paramMapping.put(paramName, i);
                }
            }
        }
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for(int i = 0 ; i <  parameterTypes.length; i ++)
        {
            Class<?> type = parameterTypes[i];
            if(HttpServletRequest.class.isAssignableFrom(type) || HttpServletResponse.class.isAssignableFrom(type))
            {
                paramMapping.put(type.getName(), i);
            }
        }

        Object[] paramValues = new Object[parameterTypes.length];

        Map<String, String[]> params = request.getParameterMap();
        for(Map.Entry<String,String[]> param : params.entrySet())
        {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replace("\\s","");
            if(!paramMapping.containsKey(param.getKey())){continue;}
            int index = paramMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        if(paramMapping.containsKey(HttpServletRequest.class.getName()))
        {
            Integer reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if(paramMapping.containsKey(HttpServletResponse.class.getName()))
        {
            int respIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }
        Object returnValue = handlerMapping.getMethod().invoke(((GPHandlerMapping) handler).getController(), paramValues);
        if(returnValue == null || returnValue instanceof  Void) { return null;}
//        boolean isModelView = returnValue instanceof GPModelAndView;
        boolean isModelView = handlerMapping.getMethod().getReturnType() == GPModelAndView.class;
        if(isModelView)
        {
            return (GPModelAndView) returnValue;
        }
        return null;
    }

    private Object caseStringValue(String value, Class<?> parameterType) {
        if(Integer.class == parameterType)
        {
            return Integer.valueOf(value);
        }
        return value;
    }

}
