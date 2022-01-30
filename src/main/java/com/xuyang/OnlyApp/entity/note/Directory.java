package com.xuyang.OnlyApp.entity.note;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Directory {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    private int level;

    private Long parent= (long) -1;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Directory> children;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Note> notes;

    private Long user;
}
