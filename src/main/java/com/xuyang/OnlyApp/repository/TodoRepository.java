package com.xuyang.OnlyApp.repository;

import com.xuyang.OnlyApp.entity.Todo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    List<Todo> getAllByUserId(Long id);

    @Query(nativeQuery = true, value = "select * from todo where user_id=?1 and finished=false order by create_time desc limit 0,?2")
    List<Todo> getUnfinishedTop(Long id, int num);

}
