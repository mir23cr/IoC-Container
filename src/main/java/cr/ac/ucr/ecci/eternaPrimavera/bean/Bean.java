package cr.ac.ucr.ecci.eternaPrimavera.bean;

import java.util.List;


import cr.ac.ucr.ecci.eternaPrimavera.enums.ComponentEnum;
import cr.ac.ucr.ecci.eternaPrimavera.enums.ScopeEnum;


/**
 * Class that stores the meta-data for the instances of the bean.
 * Also includes the instance if the Bean is Singleton.
 * @author Vladimir Aguilar
 * @author Jose Mesén
 * @author Rodrigo Acuña
 */
public class Bean
{
    private String id;
    private Object instance;
    private String classType;
    private String init;
    private String destroy;
    private ScopeEnum scopeType;
    private List<Parameter> constructorArguments;
    private List<Parameter> properties;
    private Boolean lazyInit;
    private ComponentEnum componentEnum;


    public Bean() {
        this.scopeType = ScopeEnum.SINGLETON;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getInstance() {
        return this.instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getClassType() {
        return this.classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getInit() {
        return this.init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getDestroy() {
        return this.destroy;
    }

    public void setDestroy(String destroy) {
        this.destroy = destroy;
    }

    public ScopeEnum getScopeType() {
        return this.scopeType;
    }

    public void setScopeType(ScopeEnum scopeType) {
        this.scopeType = scopeType;
    }

    public List<Parameter> getConstructorArguments() {
        return this.constructorArguments;
    }

    public void setConstructorArguments(List<Parameter> constructorArguments) {
        this.constructorArguments = constructorArguments;
    }

    public List<Parameter> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Parameter> properties) {
        this.properties = properties;
    }

    public Boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(Boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public ComponentEnum getComponentEnum() {
        return componentEnum;
    }

    public void setComponentEnum(ComponentEnum componentEnum) {
        this.componentEnum = componentEnum;
    }
}
