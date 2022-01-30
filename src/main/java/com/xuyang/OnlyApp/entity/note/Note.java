package com.xuyang.OnlyApp.entity.note;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class Note {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private Date created_time;
    private Date modified_time;

    @ManyToOne
    private Directory directory;
}
