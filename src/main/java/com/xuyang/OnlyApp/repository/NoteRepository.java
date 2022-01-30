package com.xuyang.OnlyApp.repository;

import com.xuyang.OnlyApp.entity.note.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {
}
