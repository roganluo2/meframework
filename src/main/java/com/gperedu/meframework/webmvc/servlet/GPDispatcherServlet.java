package com.gperedu.meframework.webmvc.servlet;

import com.gperedu.meframework.annotation.GPController;
import com.gperedu.meframework.annotation.GPRequestMapping;
import com.gperedu.meframework.context.GPApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by rogan on 2019/4/21.
 */
public class GPDispatcherServlet extends HttpServlet {

    private GPApplicationContext context;

    static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private List<GPHandlerMapping> handlerMapping = new ArrayList<>();

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapterMap = new HashMap<>();

    private List<GPViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    protected void initStrategies(GPApplicationContext context) {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }

    private void initFlashMapManager(GPApplicationContext context) {

    }

    private void initViewResolvers(GPApplicationContext context) {

        String templateRoot = context.getConfig().getProperty("templateRoot");
        viewResolvers.add(new GPViewResolver(templateRoot));
//        String templatePath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
//        File templateRootDir = new File(templatePath);
//        for(File tempalte : templateRootDir.listFiles())
//        {
//            viewResolvers.add(new GPViewResolver(templateRoot));
//        }
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {


    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {


    }

    private void initHandlerAdapters(GPApplicationContext context) {

        for(GPHandlerMapping gpHandlerMapping : handlerMapping)
        {
            this.handlerAdapterMap.put(gpHandlerMapping, new GPHandlerAdapter());
        }

    }

    private void initHandlerMappings(GPApplicationContext context) {

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for(String beanName : beanDefinitionNames)
        {
            Object controller = context.getBean(beanName);
            Class<?> clazz = controller.getClass();
            if(!clazz.isAnnotationPresent(GPController.class))
            {
                continue;
            }
            String url = "";
            if(clazz.isAnnotationPresent(GPRequestMapping.class))
            {
                GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                url = requestMapping.value();
            }
            Method[] methods = clazz.getMethods();
            for(Method method:methods)
            {
                if(!method.isAnnotationPresent(GPRequestMapping.class))
                {continue;}
                GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                String regex = ("/" + url + requestMapping.value()).replaceAll("\\*",".*").replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.add(new GPHandlerMapping(controller, method, pattern));
                System.out.println("mapping" + regex + "," + method);
            }
        }


    }

    private void initThemeResolver(GPApplicationContext context) {

    }

    private void initLocaleResolver(GPApplicationContext context) {


    }

    private void initMultipartResolver(GPApplicationContext context) {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new GPApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        initStrategies(context);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1.handler
        GPHandlerMapping handler = getHandler(request);

        if(handler == null){
            processDispatchResult(request, response, new GPModelAndView("404"));
            return;
        }

        GPHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        GPModelAndView mv = handlerAdapter.handle(request, response, handler);

        processDispatchResult(request, response, mv);
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, GPModelAndView mv) throws Exception {
        if(null == mv) {return;}
        if(this.viewResolvers.isEmpty()){return;}
        for(GPViewResolver viewResolver : viewResolvers)
        {
            GPView gpView = viewResolver.resolveViewName(mv.getViewName(), null);
            if(null != gpView)
            {
                gpView.render(mv.getModel(), request, response);
                return;
            }
        }
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handler) {
        if(this.handlerAdapterMap.isEmpty()) return null;
        GPHandlerAdapter handlerAdapter = handlerAdapterMap.get(handler);
        if(handlerAdapter.supports(handler)){
            return handlerAdapter;
        }
        return null;

    }

    private GPHandlerMapping getHandler(HttpServletRequest request) {
        if(this.handlerMapping.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+","/");

        for(GPHandlerMapping gpHandlerMapping :handlerMapping)
        {
            if(gpHandlerMapping.getPattern().matcher(url).matches())
            {
                return gpHandlerMapping;
            }
        }
        return null;
    }
}
