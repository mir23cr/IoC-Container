package cr.ac.ucr.ecci.eternaPrimavera.enums;

/**
 * Enum with the arguments that a cr.ac.ucr.ecci.eternaPrimavera.bean could have.
 * @author Vladimir Aguilar
 */
public enum BeanArgument {
    CONSTRUCTOR_ARG,
    PROPERTY,
    ERROR;

    public static BeanArgument getArgument(String argumentName){
        argumentName = argumentName.toLowerCase();
        BeanArgument argument = ERROR;
        switch (argumentName){
            case "constructor-arg":
                argument = CONSTRUCTOR_ARG;
                break;
            case "property":
                argument = PROPERTY;
                break;
        }
        return argument;
    }
}
