package com.qub.customproxyrouter.operator;

import javax.persistence.*;


@Entity
@Table
public class Operator {
    @Id
    @SequenceGenerator(
            name = "operator_sequence",
            sequenceName = "operator_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "operator_sequence"
    )
    private long id;
    @Column(unique=true)
    private String name;
    private String urls;

    public Operator() {

    }

    public Operator(long id, String name, String urls) {
        this.id = id;
        this.name = name;
        this.urls = urls;
    }

    public Operator(String name, String urls) {
        this.name = name;
        this.urls = urls;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", urls=" + urls +
                '}';
    }
}
