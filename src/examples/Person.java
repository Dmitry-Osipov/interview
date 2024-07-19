package examples;

import java.util.List;
import java.util.Objects;

public class Person {
    private String name;
    private List<Address> addresses;

    public Person(String name, List<Address> addresses) {
        this.addresses = addresses;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getName(), person.getName()) && Objects.equals(getAddresses(), person.getAddresses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAddresses());
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}
