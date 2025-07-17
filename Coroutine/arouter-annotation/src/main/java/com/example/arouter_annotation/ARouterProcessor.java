package com.example.arouter_annotation;

import static com.example.arouter_annotation.ARouterProcessor.MODULE;
import static com.example.arouter_annotation.ARouterProcessor.PACKAGE;
import static com.example.arouter_annotation.ProcessorConfig.GROUP_VAR1;
import static com.example.arouter_annotation.ProcessorConfig.PATH_VAR1;

import com.example.arouter_annotation.api.ARouter;
import com.example.arouter_annotation.api.ARouterPath;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


/**
 * ‼️一定要在这个目录下：
 * 📖arouter-annotation/src/main/resources/META-INF/services/javax.annotation.processing.Processor
 * 加这个文件内容，才会调用到 init()：
 * 📖com.example.arouter_annotation.ARouterProcessor
 *
 * ‼️一定要加注解
 * 🌟@SupportedAnnotationTypes
 * 才会调用到 process()
 *
 *
 * 终极步骤：
 * 💎1.添加 aRouter-annotation 的 implementation 的依赖
 * 💎2.添加 aRouter-annotation 的编辑机 annotationProcessorOptions
 * 💎3.添加项目名和包名
 * 💎4.就可以使用注解了
 */
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("com.example.arouter_annotation.api.ARouter")
@SupportedOptions({MODULE,PACKAGE})
public class ARouterProcessor extends AbstractProcessor {

    public static final String MODULE = "module";
    public static final String PACKAGE = "packageNameForAPT";
    //操作Element的工具类（类、函数、属性）
    private Elements elementTools;

    //类信息的工具类，包含用于操作TypeMirror的工具方法
    private Types typeTool;

    //用来打印日志相关信息
    private Messager messager;

    //文件生成器，类、资源等，就是最终要生成的文件，是需要Filer来完成的
    private Filer filer;

    //引用的模块名
    private String moduleName;

    //引用模块的包名
    private String packageName;

    //仓库一 组名->类名映射 列表
    private Map<String, List<RouterBean>> mAllPathMap = new HashMap<>();

    //仓库二 组名->组名列表映射
    private Map<String,String> mAllGroupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementTools = processingEnv.getElementUtils();
        typeTool = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();

        moduleName = processingEnv.getOptions().get(MODULE);
        packageName = processingEnv.getOptions().get(PACKAGE);
        messager.printMessage(Diagnostic.Kind.NOTE, "zjy>>" + moduleName + " , package = " + packageName);

    }

    //在编译时干活
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "zjy>>>>>>");

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        if( elements.isEmpty()){
            return false;
        }
        /**
         * 1.遍历所有添加了注解的类 mAllPathMap
         */
        for (Element element : elements) {
            //🌟使用APT写生成类文件
            // 路径：origincode/build/generated/ap_generated_sources/debug/out/com/universal/test

            ARouter aRouter = element.getAnnotation(ARouter.class);

            RouterBean bean = new RouterBean.Builder()
                    .addPath(aRouter.path())
                    .addGroup(aRouter.group())
                    .addElement(element)
                    .build();

            //是否是Activity类
            TypeElement actType = elementTools.getTypeElement(ProcessorConfig.ACTIVITY);
            if( typeTool.isSubtype(element.asType(),actType.asType())){
                //证明是Activity
                bean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY);
            }else {
                throw new RuntimeException("@ARouter注解仅支持Activity");
            }

            if( checkRouterPath(bean)){
                messager.printMessage(Diagnostic.Kind.NOTE,"确认注解有效");
                List<RouterBean> routerBeans = mAllPathMap.computeIfAbsent(bean.getGroup(), k -> new ArrayList<>());
                routerBeans.add(bean);
            }else {
                messager.printMessage(Diagnostic.Kind.ERROR,"@ARouter注解不符规范");
            }
        }

        TypeElement groupType = elementTools.getTypeElement(ProcessorConfig.AROUTER_API_GROUP);
        TypeElement pathType = elementTools.getTypeElement(ProcessorConfig.AROUTER_API_PATH);

        try {
            /**
             * 2. 根据 mAllPathMap 按照分组创建 PATH 方法
             */
            createPathFile(pathType);
        }catch (Exception e){
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR,"生成PATH模版时，异常 " + e);
        }

        try {
            /**
             * 3. 根据 mAllGroupMap 创建 GROUP 方法
             */
            createGroupFile(groupType,pathType);
        }catch (Exception e){
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR,"生成GROUP模版时，异常 " + e);
        }


        return false;
    }

    /**
     * 实现类缓存方法
     * @param pathType
     * @throws IOException
     */
    private void createPathFile(TypeElement pathType) throws IOException {
        if( mAllPathMap == null || mAllPathMap.isEmpty()){
            return;
        }

        TypeName typeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class)
        );

        for (Map.Entry<String, List<RouterBean>> map : mAllPathMap.entrySet()) {
            //1.方法
            MethodSpec.Builder builder = MethodSpec
                    .methodBuilder(ProcessorConfig.PATH_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(typeName);

            //$N这里是变量，$T接口
            builder.addStatement("$T<$T,$T> $N = new $T<>()",
                    ClassName.get(Map.class),ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    ProcessorConfig.PATH_VAR1,
                    ClassName.get(HashMap.class));

            for (RouterBean bean : map.getValue()) {
                builder.addStatement("$N.put($S,$T.create($T.$L,$T.class,$S,$S))",
                        ProcessorConfig.PATH_VAR1,
                        bean.getPath(),
                        ClassName.get(RouterBean.class),
                        //枚举的类型
                        ClassName.get(RouterBean.TypeEnum.class),
                        bean.getTypeEnum(),
                        //T.class，传入类的类型 MainActivity.class
                        ClassName.get((TypeElement) bean.getElement()),
                        bean.getPath(),
                        bean.getGroup());
            }
            builder.addStatement("return $N",PATH_VAR1);
            String finalClassName = ProcessorConfig.PATH_FILE_NAME + map.getKey();
            messager.printMessage(Diagnostic.Kind.NOTE,
                    "APT 生成的Path类文件" + packageName + "." +  finalClassName);

            //2.类
            TypeSpec myClass = TypeSpec
                    //类名
                    .classBuilder(finalClassName)
                    //实现接口 implements ARouterPath
                    .addSuperinterface(ClassName.get(pathType))
                    //作用域
                    .addModifiers(Modifier.PUBLIC)
                    //传入方法
                    .addMethod(builder.build())
                    .build();

            //3.包
            JavaFile.builder(packageName,myClass)
                    .build()
                    .writeTo(filer);

            mAllGroupMap.put(map.getKey(),finalClassName);
        }
    }

    /**
     * 实现组缓存方法
     * @param groupType
     */
    private void createGroupFile(TypeElement groupType,TypeElement pathType) throws IOException {
        // 构建返回类型
        // Map<String,Class<? extends ARouterPath>>
        // 🌟JavaPoet中ParameterizedTypeName是用来描述一个泛型类型的
        // 比如你需要声明方法返回值的类型，或者字段的类型，必须用它来构建完整的泛型类型。
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathType))
                )
        );

        //1.方法
        MethodSpec.Builder methodSpec = MethodSpec
                .methodBuilder(ProcessorConfig.PATH_METHOD_GROUP)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(methodReturns);

        methodSpec.addStatement("$T<$T,$T> $N = new $T<>()",
// 🌟在addStatement里，你传入的是类型参数，必须是TypeName
//   而这个TypeName可以是ClassName或者ParameterizedTypeName，所以不需要用 ParameterizedTypeName
//                ParameterizedTypeName.get(
                        ClassName.get(Map.class),
                        ClassName.get(String.class),
                        ParameterizedTypeName.get(
                                ClassName.get(Class.class),
                                WildcardTypeName.subtypeOf(ClassName.get(pathType))),
                GROUP_VAR1,
                ClassName.get(HashMap.class));

        messager.printMessage(Diagnostic.Kind.NOTE,
                "APT 生成的Grout类中间" + methodSpec);

        for (Map.Entry<String, String> map : mAllGroupMap.entrySet()) {
            methodSpec.addStatement("$N.put($S,$T.class)",
                    GROUP_VAR1,
                    map.getKey(),
                    ClassName.get(packageName,map.getValue()));

        }
        methodSpec.addStatement("return $N", GROUP_VAR1);

        String finalClassName = ProcessorConfig.GROUP_FILE_NAME + moduleName;

        messager.printMessage(Diagnostic.Kind.NOTE,
                "APT 生成的Grout类文件" + finalClassName);

        //2.类
        TypeSpec myClass = TypeSpec
                .classBuilder(finalClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(groupType))
                .addMethod(methodSpec.build())
                .build();

        //3.包
        JavaFile.builder(packageName,myClass)
                .build().writeTo(filer);

    }

    private final boolean checkRouterPath(RouterBean bean) {
        String group = bean.getGroup();
        String path = bean.getPath();

        if (!path.startsWith("/")) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解的path值，必须以/开头");
            return false;
        }


        if (path.lastIndexOf("/") == 0) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范");
            return false;
        }

        String finalGroup = path.substring(1, path.indexOf("/", 1));
        if (!group.isEmpty() && !group.equals(moduleName)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解group必须和子模块名一致");
            return false;
        } else {
            bean.setGroup(finalGroup);
            return true;
        }
    }

    /**
     * 生成ARouter文件
     *
     * 目标
     * public class ARoutActivity$$$$$$$$ARouter {
     *   public static Class findTargetClass(String path) {
     *     return path.equals("ARoutActivity") ? ARoutActivity.class : null;
     *   }
     * }
     * @param element
     * @return
     */
    private void aRouterFile(Element element){
        ARouter aRouter = element.getAnnotation(ARouter.class);

        //1.方法
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("findTargetClass")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(String.class,"path")
                .returns(Class.class)
                .addStatement("return path.equals($S) ? $T.class : null"
                        ,aRouter.path()
                        ,ClassName.get((TypeElement)element))
                .build();

        //2.类
        String className = element.getSimpleName().toString();
        String finalClassName = className + "$$$$$$$$ARouter";
        TypeSpec myClass = TypeSpec
                .classBuilder(finalClassName)
                .addMethod(methodSpec)
                .addModifiers(Modifier.PUBLIC)
                .build();

        //3.包
        String packageName = elementTools.getPackageOf(element).getQualifiedName().toString();
        JavaFile javaFile = JavaFile
                .builder(packageName,myClass)
                .build();

        //4.生成文件
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, "zjy>>>>>>error:" + e);
        }
    }


    /**
     * 生成普通文件
     * 目标
     *     public final class BabyTest {
     *       public static void main(String[] args) {
     *         System.out.println("Hello,BabyZJY!");
     *       }
     *     }
     * @return
     */
    private void normalFile(){
        //1.方法
        MethodSpec mainMethod = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello,BabyZJY!")
                .build();

        //2.类
        TypeSpec testClass = TypeSpec.classBuilder("BabyTest")
                .addMethod(mainMethod)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build();
        //3.包
        JavaFile packegef = JavaFile.builder
                        ("com.universal.test", testClass)
                .build();
        //4.生成文件
        try {
            packegef.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, "zjy>>>>>>error:" + e);
        }
    }
}
