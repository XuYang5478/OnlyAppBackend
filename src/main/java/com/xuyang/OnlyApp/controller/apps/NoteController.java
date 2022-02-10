package com.xuyang.OnlyApp.controller.apps;

import com.xuyang.OnlyApp.entity.note.Directory;
import com.xuyang.OnlyApp.entity.note.Note;
import com.xuyang.OnlyApp.repository.DirectoryRepository;
import com.xuyang.OnlyApp.repository.NoteRepository;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/note")
@Transactional
public class NoteController {

    private DirectoryRepository directoryRepository;
    private NoteRepository noteRepository;

    public NoteController(DirectoryRepository directoryRepository, NoteRepository noteRepository) {
        this.directoryRepository = directoryRepository;
        this.noteRepository = noteRepository;
    }

    @GetMapping("/all_dirs")
    public List<Directory> getAllDir(@RequestParam Long userId) {
        Directory default_dir = directoryRepository.findByNameAndUser("默认文件夹", userId);
        if (default_dir == null) {
            Directory new_default = new Directory();
            new_default.setUser(userId);
            new_default.setName("默认文件夹");
            new_default.setLevel(0);
            directoryRepository.save(new_default);
        }
        return directoryRepository.findAllByUserAndLevel(userId, 0);
    }

    @PostMapping("/create_dir")
    public List<Directory> createNewDir(@RequestBody Map<String, String> info) {
        Long userId = Long.parseLong(info.get("userId"));

        Directory one = new Directory();
        one.setUser(userId);
        one.setName(info.get("name"));
        long current_dir = -1;

        try {
            current_dir = Long.parseLong(info.get("current_dir"));
        } catch (Exception ignored) { }

        Directory current = directoryRepository.findByIdAndUser(current_dir, userId);
        if (current == null) {
            one.setLevel(0);
            directoryRepository.save(one);
        } else {
            one.setLevel(current.getLevel() + 1);
            one.setParent(current_dir);
            current.getChildren().add(one);
            directoryRepository.save(current);
        }
        return directoryRepository.findAllByUserAndLevel(userId, 0);
    }

    @PostMapping("/rename_dir")
    public List<Directory> renameDir(@RequestBody Map<String, String> dir){
        long userId=Long.parseLong(dir.get("userId"));
        long dirId=Long.parseLong(dir.get("id"));

        Directory directory = directoryRepository.findByIdAndUser(dirId, userId);
        directory.setName(dir.get("name"));
        directoryRepository.save(directory);

        return directoryRepository.findAllByUserAndLevel(userId, 0);
    }

    @PostMapping("/delete_dir")
    public List<Directory> deleteDir(@RequestBody Map<String, String> dir){
        long userId=Long.parseLong(dir.get("userId"));
        long dirId=Long.parseLong(dir.get("id"));

        Directory deleted=directoryRepository.findByIdAndUser(dirId, userId);
        Directory parent=directoryRepository.findByIdAndUser(deleted.getParent(), userId);
        if(parent!=null){
            parent.getChildren().remove(deleted);
            directoryRepository.save(parent);
        }
        directoryRepository.deleteByIdAndUser(dirId, userId);

        return directoryRepository.findAllByUserAndLevel(userId, 0);
    }

    @GetMapping("/all_notes")
    public List<Note> getAllNotes(@RequestParam String userId, @RequestParam String dirId) {
        long user=-1;
        long dir=-1;
        try {
            user=Long.parseLong(userId);
            dir=Long.parseLong(dirId);
        }catch (Exception e){
            return null;
        }

        if(dir==-1){
            return noteRepository.findAllByUserId(user);
        }

        Directory directory=directoryRepository.findByIdAndUser(dir, user);
        if(directory!=null){
            return directory.getNotes();
        }else {
            return null;
        }
    }
}
