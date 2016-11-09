package model;

import dao.DaoFilter;
import pool.ConnectionPool;

/**
 * Created by Freemind on 2016-11-09.
 */
public class Specialisation {

    private int id;
    private int root;
    private String name;


    public Specialisation(int id, int root, String name) {
        this.id = id;
        this.root = root;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return "Specialisation{" +
                "id=" + id +
                ", root=" + root +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Specialisation that = (Specialisation) o;

        if (getId() != that.getId()) return false;
        if (getRoot() != that.getRoot()) return false;
        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getRoot();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
