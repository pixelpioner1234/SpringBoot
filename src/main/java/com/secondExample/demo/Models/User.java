package com.secondExample.demo.Models;

import java.util.Objects;

public class User {

    public String name;
    public String pass;
    public Role role;

    public User() {
    }

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public User(String name, String pass, Role role) {
        this.name = name;
        this.pass = pass;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(pass, user.pass) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pass, role);
    }
}
