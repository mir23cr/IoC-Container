package family;

import family.pets.Cat;
import family.pets.Dog;

/**
 * @author Vladimir Aguilar
 * Creation Date: 18/9/2017
 */
public class House {
    private Person dad;
    private Dog doggie;
    private Cat cat;

    public House(){
        //dad = new Person();
    }

    public House(Dog dog, Cat cat){
        this.doggie = dog;
        this.cat = cat;
        this.dad = new Person();
        dad.setName("Bartosz");
    }

    public House(Dog dog, Cat cat, Person dad){
        this.doggie = dog;
        this.cat = cat;
        this.dad = dad;
    }

    public Person getDad() {
        return dad;
    }

    public void setDad(Person padre) {
        this.dad = padre;
    }

    public Dog getDoggie() {
        return doggie;
    }

    public void setDoggie(Dog doggie) {
        this.doggie = doggie;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public void run(){
        System.out.println("Building house...");
    }

    public void close(){
        this.cat = null;
        System.out.println("Destroying house...");
    }
}
