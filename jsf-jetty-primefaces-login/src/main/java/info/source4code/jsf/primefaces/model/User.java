package info.source4code.jsf.primefaces.model;

public class User {

    private String firstName;
    private String lastName;

    public User(String firstName, String lastName) {
        super();

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String toString() {
        return "user[firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
