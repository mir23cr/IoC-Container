package parsers;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * @author Rodrigo Acuña
 * @author Vladimir Aguilar
 * @author José Mesén
 * Creation Date: 9/9/2017
 */
public class AnnotationParser implements Parser {
    String basePackagePath;

    public AnnotationParser(String packageLocation){
        //linea
        String classesRootDirectory = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File basePackage = new File(classesRootDirectory+packageLocation.replaceAll(".","/"));
        basePackagePath = basePackage.getAbsolutePath();
        System.out.println(basePackagePath);

        if(!basePackage.exists() || !basePackage.isDirectory())
            System.out.println("Incorrect Base Package");
        else {
            scanPackage(basePackage);
        }
    }

    public void scanPackage(File currentPackage){
        File[] packageContents = currentPackage.listFiles();
        for(File file: packageContents){
            if(file.isDirectory())
                scanPackage(file);
            else{
                String className = file.getName().replace(".class","");
                String currentPath = currentPackage.getPath().substring(basePackagePath.length()+1);
                String classPath = currentPath.replace("\\",".") + "." + className;
                scanClass(classPath);

            }
        }

    }

    private void scanClass(String classPath) {
        try {
            Class currentClass = Class.forName(classPath);
            Annotation[] annotations = currentClass.getAnnotations();
            for (Annotation annotation:annotations) {
                System.out.println(annotation.annotationType());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
