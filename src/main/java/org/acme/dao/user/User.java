package org.acme.dao.user;

public class User {
    private String surname;
    private String name;
    private long id;

    public User(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public User() {

    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
