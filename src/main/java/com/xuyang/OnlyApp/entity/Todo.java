package com.xuyang.OnlyApp.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean finished;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date finishTime;

    public Todo(Long userId, String content) {
        this.userId = userId;
        this.content = content;
        this.finished = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Todo todo = (Todo) o;

        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return 177241809;
    }
}
