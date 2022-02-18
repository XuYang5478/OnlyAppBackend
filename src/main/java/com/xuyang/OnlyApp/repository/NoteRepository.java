package com.xuyang.OnlyApp.repository;

import com.xuyang.OnlyApp.entity.note.Note;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {

    List<Note> findAllByUserId(Long userId);

    List<Note> findAllByUserId(Long userId, Pageable pageable);

    @Query(value = "select * from note where user_id=?1 order by 'modified_time', id desc limit 0, ?2", nativeQuery = true)
    List<Note> findRecent(Long userId, int num);

    Note findByIdAndUserId(long noteId, long userId);

    List<Note> findAllByUserIdAndTitleLike(Long userId, String keyword);

    List<Note> findAllByUserIdAndContentLike(Long userId, String keyword);
}
