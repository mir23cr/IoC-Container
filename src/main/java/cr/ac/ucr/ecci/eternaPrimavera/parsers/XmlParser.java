package cr.ac.ucr.ecci.eternaPrimavera.parsers;


import java.io.IOException;
import java.util.*;
import cr.ac.ucr.ecci.eternaPrimavera.enums.AutowireMode;
import cr.ac.ucr.ecci.eternaPrimavera.bean.Bean;
import cr.ac.ucr.ecci.eternaPrimavera.bean.Parameter;
import cr.ac.ucr.ecci.eternaPrimavera.enums.ScopeEnum;
import cr.ac.ucr.ecci.eternaPrimavera.enums.BeanArgument;
import cr.ac.ucr.ecci.eternaPrimavera.enums.BeanProperty;
import cr.ac.ucr.ecci.eternaPrimavera.enums.ParameterElement;
import cr.ac.ucr.ecci.eternaPrimavera.parsers.AnnotationParser;
import nu.xom.*;

/**
 * Parser for the XML Files.
 * @author Rodrigo Acuña
 * @author Vladimir Aguilar
 * @author José Mesén
 */
public class XmlParser implements Parser {

    private Builder parser;
    private Map<String,Bean> beansDefinition;
    private String fileName;
    private Boolean defaultLazyInit;

    public XmlParser(String fileName){
        //this.fileName = System.getProperty("user.dir") + fileName;
        this.fileName = fileName;
        this.parser = new Builder();
        this.beansDefinition = new HashMap<String, Bean>();
    }

    /**
     * Get the default init method name
     * @return the name of the init method or null if it not exist
     * */
    public String getDefaultInitMethod() throws ParsingException, IOException {
        //Document document = parser.build(this.fileName);
        Document document = parser.build(this.getClass().getClassLoader().getResourceAsStream(this.fileName));
        Element root = document.getRootElement();
        Attribute attribute = root.getAttribute(ParserStringConstants.BEANS_DEFAULT_INIT);
        return (attribute!=null)? attribute.getValue():null;
    }

    /**
     * Get the default destroy method name
     * @return the name of the init method or null if it not exist
     * */
    public String getDefaultDestroyMethod() throws ParsingException, IOException {
        //Document document = parser.build(this.fileName);
        Document document = parser.build(this.getClass().getClassLoader().getResourceAsStream(this.fileName));
        Element root = document.getRootElement();
        Attribute attribute = root.getAttribute(ParserStringConstants.BEANS_DEFAULT_DESTROY);
        return (attribute!=null)? attribute.getValue():null;
    }


    /**
     * Get all the beans in the XML files and in cr.ac.ucr.ecci.eternaPrimavera.annotations if they are expressed in the file.
     * @return the map with the beans in the way <id,Bean>
     * */
    @Override
    public Map<String, Bean> getBeans(){
        try {
            //Document document = parser.build(this.fileName);
            Document document = parser.build(this.getClass().getClassLoader().getResourceAsStream(this.fileName));
            Element root = document.getRootElement();
            Attribute lazyInit = root.getAttribute(ParserStringConstants.BEANS_SCAN_LAZY_INIT);
            if(lazyInit != null){
                this.defaultLazyInit = Boolean.parseBoolean(lazyInit.getValue());
            }

            Element annotations = root.getFirstChildElement(ParserStringConstants.BEANS_SCAN_ANNOTATIONS);
            if(annotations != null){
                AnnotationParser annotationParser = new AnnotationParser(
                        annotations.getAttribute(ParserStringConstants.BEANS_SCAN_ANNOTATIONS_PACKAGE).getValue());
                this.beansDefinition = annotationParser.getBeans();
                if(this.beansDefinition == null){
                    this.beansDefinition = new HashMap<String, Bean>();
                }
            }
            Elements children = root.getChildElements(ParserStringConstants.BEAN_LABEL);
            Bean newBean;

            for (int i = 0; i < children.size(); i++) {
                newBean = this.createBean(children.get(i));
                if(newBean.isLazyInit() == null){
                    newBean.setLazyInit((this.defaultLazyInit != null)?this.defaultLazyInit:false);
                }
                this.beansDefinition.put(newBean.getId(),newBean);
            }
            return this.beansDefinition;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create an instance of a cr.ac.ucr.ecci.eternaPrimavera.bean from the data in the file.
     * @param beanDefinition the tag in the file with the cr.ac.ucr.ecci.eternaPrimavera.bean information
     * @return the instance of the cr.ac.ucr.ecci.eternaPrimavera.bean with the attributes setted
     * */
    public Bean createBean(Element beanDefinition){
        try{
            Bean newBean = new Bean();
            BeanProperty beanProperty;
            String propertyValue;
            for(int j = 0; j < beanDefinition.getAttributeCount(); j++) {
                beanProperty = BeanProperty.getProperty(beanDefinition.getAttribute(j).getLocalName());
                propertyValue = beanDefinition.getAttribute(j).getValue();
                switch (beanProperty){
                    case ID:
                        newBean.setId(propertyValue);
                        break;
                    case CLASS:
                        newBean.setClassType(propertyValue);
                        break;
                    case INIT:
                        newBean.setInit(propertyValue);
                        break;
                    case DESTROY:
                        newBean.setDestroy(propertyValue);
                        break;
                    case SCOPE:
                        newBean.setScopeType(ScopeEnum.valueOf(propertyValue.toUpperCase()));
                        break;
                    case LAZYINIT:
                        newBean.setLazyInit(Boolean.parseBoolean(propertyValue));
                        break;
                    default:
                        break;
                }
            }

            this.getBeanArgs(beanDefinition.getChildElements(), newBean);

            return newBean;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set in the cr.ac.ucr.ecci.eternaPrimavera.bean the constructor arguments and the setters parameters.
     * @param beanArgs the tag with the paramenters
     * @param newBean the cr.ac.ucr.ecci.eternaPrimavera.bean in which the parameters have to be instanciated
     * */
    public void getBeanArgs(Elements beanArgs, Bean newBean){
        Element argument;
        Parameter newParameter;
        List<Parameter> constructorArgs = new ArrayList<Parameter>();
        List<Parameter> properties = new ArrayList<Parameter>();
        for(int i =0; i < beanArgs.size(); i++){
            argument = beanArgs.get(i);
            switch (BeanArgument.getArgument(argument.getLocalName())){
                case CONSTRUCTOR_ARG:
                    //System.out.println("Varas del constructor ");
                    newParameter = this.getConstructorInformation(argument);
                    constructorArgs.add(newParameter);
                    break;
                case PROPERTY:
                    //System.out.println("Varas de properties ");
                    newParameter = this.getPropertiesInformation(argument);
                    properties.add(newParameter);
                    break;
                case ERROR:
                    //System.out.println("Error!");
                    break;
            }
        }
        if(constructorArgs.size() > 0 && constructorArgs.get(0).getIndex() != null){
            constructorArgs.sort(new ParameterIndexComparator());
        }
        newBean.setConstructorArguments(constructorArgs);
        newBean.setProperties(properties);
    }

    /**
     * Set a parameter with the information of the constructor.
     * @param info the tag with the information of the constructor parameter
     * @return a parameter set for the constructor
     * */
    private Parameter getConstructorInformation(Element info){
        Parameter parameter = new Parameter();
        ParameterElement parameterElement;
        String constructorElementValue;
        for (int i = 0; i < info.getAttributeCount(); i++) {
            parameterElement = ParameterElement.valueOf(info.getAttribute(i).getLocalName().toUpperCase());
            constructorElementValue = info.getAttribute(i).getValue();
            switch (parameterElement){
                case REF:
                    parameter.setBeanRef(constructorElementValue);
                    parameter.setAutowireMode(AutowireMode.BYNAME);
                    break;
                case NAME:
                    parameter.setName(constructorElementValue);
                    break;
                case INDEX:
                    parameter.setIndex(Integer.parseInt(constructorElementValue));
                    break;
                case CLASS:
                    parameter.setClassTypeName(constructorElementValue);
                    parameter.setAutowireMode(AutowireMode.BYTYPE);
                    break;
            }
        }
        return parameter;
    }

    /**
     * Set a parameter with the information of the setter method.
     * @param info the tag with the information of the setter method
     * @return a parameter set for the setter method
     * */
    private Parameter getPropertiesInformation(Element info){
        Parameter parameter = new Parameter();
        ParameterElement parameterElement;
        String propertyElementValue;
        for (int i = 0; i < info.getAttributeCount(); i++) {
            parameterElement = ParameterElement.valueOf(info.getAttribute(i).getLocalName().toUpperCase());
            propertyElementValue = info.getAttribute(i).getValue();
            switch (parameterElement){
                case REF:
                    parameter.setBeanRef(propertyElementValue);
                    parameter.setAutowireMode(AutowireMode.BYNAME);
                    break;
                case NAME:
                    parameter.setName(propertyElementValue);
                    break;
                case CLASS:
                    parameter.setClassTypeName(propertyElementValue);
                    parameter.setAutowireMode(AutowireMode.BYTYPE);
                    break;
            }
        }
        return parameter;
    }

}
