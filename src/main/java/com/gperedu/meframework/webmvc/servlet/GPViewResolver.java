package com.gperedu.meframework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

/**
 * Created by rogan on 2019/4/22.
 */
public class GPViewResolver {

    private final  String DEFAULT_TEMPLATE_SUFFIX = ".html";


    private File templateRootDir;

    private String viewName;

    public GPViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    GPView resolveViewName(String viewName, Locale locale) throws Exception{
        this.viewName = viewName;
        if(null == viewName || "".equals(viewName.trim())){return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)? viewName : viewName + DEFAULT_TEMPLATE_SUFFIX;
        File templateFile = new File(templateRootDir.getPath() + "/" + viewName.replaceAll("/+","/"));
        return  new GPView(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}
