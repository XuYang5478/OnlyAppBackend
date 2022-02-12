package com.xuyang.OnlyApp.entity.note;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Note {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private Date created_time;

    @UpdateTimestamp
    @Column(columnDefinition = "timestamp")
    private Date modified_time;

    private Long userId;
}
