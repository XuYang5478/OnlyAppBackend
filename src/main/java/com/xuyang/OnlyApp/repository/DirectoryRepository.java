package com.xuyang.OnlyApp.repository;

import com.xuyang.OnlyApp.entity.note.Directory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DirectoryRepository extends CrudRepository<Directory, Long> {

    Directory findByNameAndUser(String name, Long userId);
    Directory findByIdAndUser(Long id, Long userId);

    void deleteByIdAndUser(Long id, Long userId);

    List<Directory> findAllByUserAndLevel(Long userId, int level);
}
