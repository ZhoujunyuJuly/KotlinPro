package com.example.arouter_annotation;

import com.example.arouter_annotation.parameter.Parameter;
import com.example.arouter_annotation.parameter.ParameterFactory;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
@SupportedAnnotationTypes(ProcessorConfig.PARAMETER_CLASS)
public class ParameterProcessor extends AbstractProcessor {

    private Elements elementsUtils;
    private Types types;
    private Messager messager;
    private Filer filer;
    private Map<TypeElement, List<Element>> parameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementsUtils = processingEnv.getElementUtils();
        types = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Parameter.class);

        for (Element element: elements) {
            //返回上一层元素
            TypeElement classElement = (TypeElement)element.getEnclosingElement();
            if(parameterMap.containsKey(classElement)){
                parameterMap.get(classElement).add(element);
            }else {
                List<Element> elementList = new ArrayList<>();
                elementList.add(element);
                parameterMap.put(classElement,elementList);
            }
        }

        if( parameterMap.isEmpty()){
            return true;
        }

        TypeElement activityType = elementsUtils.getTypeElement(ProcessorConfig.ACTIVITY);
        TypeElement parameterType = elementsUtils.getTypeElement(ProcessorConfig.PARAMETER_PARAMETER_PARAMETER_GET);

        ParameterSpec parameterSpec = ParameterSpec
                .builder(TypeName.OBJECT,"targetParameter")
                .build();

        for (Map.Entry<TypeElement, List<Element>> map : parameterMap.entrySet()) {
            TypeElement key = map.getKey();
            if( !types.isSubtype(key.asType(),activityType.asType())){
                throw new RuntimeException("@Parameter注解仅支持Activity");
            }

            ClassName className = ClassName.get(key);
            ParameterFactory factory = new ParameterFactory.Builder(parameterSpec)
                    .setMessager(messager)
                    .setClassName(className)
                    .build();
        }


        return false;
    }
}
