package com.zhengyinuo.froute_compile;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public abstract class BaseProcessor extends AbstractProcessor {
    //创建文件
    protected Filer filer;
    //日志信息
    protected Messager messager;
    //元素节点信息
    protected Elements elementUtils;
    //module的名字，用于区分module
    protected String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        Map<String, String> options = processingEnvironment.getOptions();
        if(!CollectionUtil.isEmpty(options)){
            moduleName = options.get(FConfig.ModuleName);
        }else{
            throw new RuntimeException("FRouter::Compiler: No ModuleName please set it in the module build.gradle ");
        }

    }

    public void printWarning(String message){
        message = "FRouter Compiler" + message + " ====";
        messager.printMessage(Diagnostic.Kind.WARNING,message);
    }

    public void printError(String message){
        messager.printMessage(Diagnostic.Kind.ERROR,message);
    }



}
