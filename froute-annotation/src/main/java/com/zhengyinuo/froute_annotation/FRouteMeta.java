package com.zhengyinuo.froute_annotation;

import javax.lang.model.element.TypeElement;

public class FRouteMeta {
    //跳转的路径
    public String path;
    //用于创建字节码对象
    public TypeElement typeElement;
    //跳转的字节码对象
    public Class<?> destination;

    private FRouteMeta(String path, TypeElement typeElement, Class<?> destination) {
        this.path = path;
        this.typeElement = typeElement;
        this.destination = destination;
    }

    public static FRouteMeta build(String path, Class<?> destination) {
        return new FRouteMeta(path,null,destination);
    }

    public static Builder setPath(String path){
        return new Builder(path);
    }

    public static class Builder {
        private String path;
        private TypeElement typeElement;
        private Class<?> destination;

        public Builder(String path) {
            this.path = path;
        }

        public Builder setTypeElement(TypeElement typeElement) {
            this.typeElement = typeElement;
            return this;
        }

        public Builder setClass(Class<?> destination) {
            this.destination = destination;
            return this;
        }


        public FRouteMeta build() {
            return new FRouteMeta(path,typeElement,destination);
        }
    }


}
