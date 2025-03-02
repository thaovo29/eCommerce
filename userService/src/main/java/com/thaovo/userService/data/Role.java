package com.thaovo.userService.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="roles")
public class Role {
    private Long id;
    private String name;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
