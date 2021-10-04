package com.xuyang.OnlyApp.controller.apps;

import com.xuyang.OnlyApp.entity.Todo;
import com.xuyang.OnlyApp.repository.TodoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/all")
    public List<Todo> getAllTodos(@RequestParam Long userId) {
        // TODO: 2021/9/12 帮前端排好序，按添加时间倒序排列，后加的在前面 
        return todoRepository.getAllByUserId(userId);
    }

    @PostMapping("/add")
    public Todo addTodo(@RequestBody Map<String, Object> data) {
        Long userId = Long.parseLong(data.get("userId").toString());
        String content = data.get("content").toString();
        return todoRepository.save(new Todo(userId, content));
    }

    @PostMapping("/edit")
    public Todo editTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @GetMapping("/delete")
    public void deleteTodo(@RequestParam String id) {
        todoRepository.deleteById(Long.parseLong(id));
    }

    @GetMapping("/unfinished_top")
    public List<Todo> getUnfinishedTodos(@RequestParam Long userId, @RequestParam int num) {
        return todoRepository.getUnfinishedTop(userId, num);
    }
}
