package com.elhazent.education.rxjava.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Repo {
    String name;
    String description;

    public Repo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
