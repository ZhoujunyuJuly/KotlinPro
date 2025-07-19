package com.example.arouter_annotation.parameter;

import com.example.arouter_annotation.ProcessorConfig;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class ParameterFactory {

    private MethodSpec.Builder method;
    private ClassName className;
    private Messager messager;

    private ParameterFactory(Builder builder){
        this.messager = builder.messager;
        this.className = builder.className;

        method = MethodSpec.methodBuilder(ProcessorConfig.PARAMETER_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(builder.parameterSpec);
    }

    public ParameterFactory build(){
        return this;
    }

    public static class Builder{

        private ParameterSpec parameterSpec;
        private ClassName className;
        private Messager messager;

        public Builder(ParameterSpec parameterSpec){
            this.parameterSpec = parameterSpec;
        }


        public ParameterSpec getParameterSpec() {
            return parameterSpec;
        }

        public Builder setParameterSpec(ParameterSpec parameterSpec) {
            this.parameterSpec = parameterSpec;
            return this;
        }

        public ClassName getClassName() {
            return className;
        }

        public Builder setClassName(ClassName className) {
            this.className = className;
            return this;
        }

        public Messager getMessager() {
            return messager;
        }

        public Builder setMessager(Messager messager) {
            this.messager = messager;
            return this;
        }

        public ParameterFactory build(){
            return new ParameterFactory(this);
        }
    }
}
