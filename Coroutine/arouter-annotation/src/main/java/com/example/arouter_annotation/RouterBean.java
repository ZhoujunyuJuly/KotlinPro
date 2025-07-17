package com.example.arouter_annotation;

import javax.lang.model.element.Element;

public class RouterBean {
    public enum TypeEnum{
        ACTIVITY
    }

    private TypeEnum typeEnum;
    private Element element;
    private Class<?> myClass;
    private String path;
    private String group;

    public RouterBean() {
    }

    public RouterBean(Builder builder){
        element = builder.element;
        myClass = builder.myClass;
        path = builder.path;
        group = builder.group;
    }

    public static RouterBean create(TypeEnum type,Class<?> myClass,String path,String group){
        RouterBean bean = new RouterBean();
        bean.typeEnum = type;
        bean.myClass = myClass;
        bean.path = path;
        bean.group = group;
        return bean;
    }

    public static class Builder {
        private Element element;
        private Class<?> myClass;
        private String path;
        private String group;

        public Builder addElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder addMyClass(Class<?> myClass) {
            this.myClass = myClass;
            return this;
        }

        public Builder addPath(String path) {
            this.path = path;
            return this;
        }

        public Builder addGroup(String group) {
            this.group = group;
            return this;
        }

        public RouterBean build(){
            return new RouterBean(this);
        }
    }

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Class<?> getMyClass() {
        return myClass;
    }

    public void setMyClass(Class<?> myClass) {
        this.myClass = myClass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
