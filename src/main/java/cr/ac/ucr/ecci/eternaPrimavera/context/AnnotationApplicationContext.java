package cr.ac.ucr.ecci.eternaPrimavera.context;

import cr.ac.ucr.ecci.eternaPrimavera.parsers.AnnotationParser;

/**
 * Application cr.ac.ucr.ecci.eternaPrimavera.context for Annotations based container.
 * @author Vladimir Aguilar
 * @author Jose Mesén
 */

public class AnnotationApplicationContext extends ApplicationContext {
    private AnnotationParser annotationParser;

    public AnnotationApplicationContext(String fileName){
        try {
            this.annotationParser = new AnnotationParser(fileName);
            this.registerBeans();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void registerBeans() {
        try {
            this.container = this.annotationParser.getBeans();
            this.setBeanSettings();
            this.injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
