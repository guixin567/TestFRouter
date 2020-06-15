package com.zhengyinuo.froute_compile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.zhengyinuo.froute_annotation.FRoute;
import com.zhengyinuo.froute_annotation.FRouteMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

//声明注解
@AutoService(Processor.class)
//设置版本号
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//设置要处理的注解
@SupportedAnnotationTypes(FConfig.FRoutePath)
//从build.gradle 传过来的信息
@SupportedOptions(FConfig.ModuleName)
public class FRouteProcessor extends BaseProcessor {
    //同一个module 对应的所有的注解的相关信息(path,字节码对象)
    private ArrayList<FRouteMeta> fRouteMetas = new ArrayList<>();

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) return false;
        printWarning("FRoute process");

        //一，遍历注解节点，生成节点信息集（注解指定路径，注解的TypeElement）
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(FRoute.class);
        TypeElement iRoutePathElement = elementUtils.getTypeElement(FConfig.IRoutePath);
        for (Element element : elements) {
            FRoute FRouteAnnotation = element.getAnnotation(FRoute.class);
            String fRouteAnnotationPath = FRouteAnnotation.path();
            TypeElement fRouteElement = (TypeElement) element;
            fRouteMetas.add(FRouteMeta.setPath(fRouteAnnotationPath)
            .setTypeElement(fRouteElement)
            .build());
        }
        printWarning("createPathFile");
        createPathFile(iRoutePathElement);
        return true;
    }

    private void createPathFile(TypeElement iRoutePathElement) {
        //二，通过JavaPoet生成FRoute$$Group$$Detail类的模板文件
        //生成顺序：先生成方法，再生成类，最后生成文件
        //方法规律：
        // 修饰符 返回类型 方法名 形参(形参类型 形参) 语句
        /*
         * public class FRoute$$Group$$Detail implements IRoutePath{
         *
         *     @override
         *     public void loadInfo(HashMap<String, FRouteMeta> routeMap)  {
         *         routeMap.put("path1", FRouteMeta.build("path1",Destination.class));
         *         routeMap.put("path2", FRouteMeta.build("path2",Destination.class));
         *     }
         * }
         */


        //1，创建方法

        //HashMap<String, FRouteMeta>
        ParameterizedTypeName routeMapName = ParameterizedTypeName.get(HashMap.class, String.class, FRouteMeta.class);
        //HashMap<String, FRouteMeta> routeMap
        ParameterSpec routeMap = ParameterSpec.builder(routeMapName, FConfig.routeMap).build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(FConfig.loadInfo)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(routeMap);

        for(FRouteMeta fRouteMeta:fRouteMetas){
            methodBuilder.addStatement("$N.put($S,$T.build($S,$T.class))",FConfig.routeMap,fRouteMeta.path,ClassName.get(FRouteMeta.class),fRouteMeta.path,ClassName.get(fRouteMeta.typeElement));
        }

        MethodSpec loadInfoMethod = methodBuilder.build();
        printWarning("loadInfoMethod");
        //2,创建类
        //类名：前缀 + module指定的名字
        String className = FConfig.FRoute$$Group$$ + moduleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(iRoutePathElement))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(loadInfoMethod)
                .build();

        printWarning("typeSpec");

        //3,生成文件
        try {
            JavaFile.builder(FConfig.packageName, typeSpec)
                    .build()
                    .writeTo(filer);
            printWarning("filer");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
