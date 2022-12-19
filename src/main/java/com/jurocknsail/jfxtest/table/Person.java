package com.jurocknsail.jfxtest.table;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {
    
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleObjectProperty<Gender> gender = new SimpleObjectProperty<>();
    private SimpleBooleanProperty alive = new SimpleBooleanProperty();

    public Person(String name, Gender gender, boolean alive) {
        this.name.set(name);
        this.gender.set(gender);
        this.alive.set(alive);
    }

    public String getName() {
        return name.get();
    }
    public Gender getGender() {
        return gender.get();
    }
    public boolean getAlive() {
        return alive.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public void setGender(Gender gender) {
        this.gender.set(gender);
    }
    public void setAlive(boolean alive) {
        this.alive.set(alive);
    }
    public SimpleStringProperty nameProperty(){
        return name;
    }
    public SimpleObjectProperty<Gender> genderProperty(){
        return gender;
    }
    public SimpleBooleanProperty aliveProperty(){
        return alive;
    }

    @Override
    public String toString() {
        return "\nPerson [name=" + name.get() + ", gender=" + gender.getValue()+ ", alive=" + alive + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((alive == null) ? 0 : alive.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        if (alive == null) {
            if (other.alive != null)
                return false;
        } else if (!alive.equals(other.alive))
            return false;
        return true;
    }

    
}
