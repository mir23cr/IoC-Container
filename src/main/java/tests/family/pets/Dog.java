package tests.family.pets;

import annotations.Component;
import annotations.Controller;
import annotations.Repository;

/**
 * @author Vladimir Aguilar
 * Creation Date: 18/9/2017
 */
@Repository("doggy")
public class Dog {
    private String name = "Pili";

    public Dog() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void init(){
        System.out.println("Initializing dog...");
    }

    public void  destroy(){
        System.out.println("Destroying dog...");
    }
}
