package com.xuyang.OnlyApp.controller.apps;

import com.xuyang.OnlyApp.entity.note.Directory;
import com.xuyang.OnlyApp.entity.note.Note;
import com.xuyang.OnlyApp.repository.DirectoryRepository;
import com.xuyang.OnlyApp.repository.NoteRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
        } catch (Exception ignored) {
        }

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
    public List<Directory> renameDir(@RequestBody Map<String, String> dir) {
        long userId = Long.parseLong(dir.get("userId"));
        long dirId = Long.parseLong(dir.get("id"));

        Directory directory = directoryRepository.findByIdAndUser(dirId, userId);
        directory.setName(dir.get("name"));
        directoryRepository.save(directory);

        return directoryRepository.findAllByUserAndLevel(userId, 0);
    }

    @PostMapping("/delete_dir")
    public List<Directory> deleteDir(@RequestBody Map<String, String> dir) {
        long userId = Long.parseLong(dir.get("userId"));
        long dirId = Long.parseLong(dir.get("id"));

        Directory deleted = directoryRepository.findByIdAndUser(dirId, userId);
        Directory parent = directoryRepository.findByIdAndUser(deleted.getParent(), userId);
        if (parent != null) {
            parent.getChildren().remove(deleted);
            directoryRepository.save(parent);
        }
        directoryRepository.deleteByIdAndUser(dirId, userId);

        return directoryRepository.findAllByUserAndLevel(userId, 0);
    }

    @GetMapping("/all_notes")
    public List<Note> getAllNotes(@RequestParam String userId, @RequestParam String dirId) {
        long user = -1;
        long dir = -1;
        try {
            user = Long.parseLong(userId);
            dir = Long.parseLong(dirId);
        } catch (Exception e) {
            return null;
        }

        if (dir == -1) {
            return noteRepository.findAllByUserId(user);
        }

        Directory directory = directoryRepository.findByIdAndUser(dir, user);
        if (directory != null) {
            return directory.getNotes();
        } else {
            return null;
        }
    }

    @PostMapping("/add_note")
    public boolean addNote(@RequestBody Map<String, String> info) {
        try {
            Long noteId=Long.parseLong(info.get("noteId"));
            Long userId = Long.parseLong(info.get("userId"));
            Long dirId = Long.parseLong(info.get("dirId"));
            String title = info.get("header");
            String content = info.get("content");

            if(noteId>0){
                Note one=noteRepository.findByIdAndUserId(noteId, userId);
                one.setTitle(title);
                one.setContent(content);
                noteRepository.save(one);
            }else{
                Directory dir = directoryRepository.findByIdAndUser(dirId, userId);
                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setUserId(userId);
                dir.getNotes().add(note);
                directoryRepository.save(dir);
            }

            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @GetMapping("/get_note")
    public Note getNote(@RequestParam String userId, @RequestParam String noteId){
        try{
            long note_id=Long.parseLong(noteId);
            long user_id=Long.parseLong(userId);

            return noteRepository.findByIdAndUserId(note_id, user_id);
        }catch (Exception e){
            return null;
        }
    }

    @PostMapping("/delete_note")
    public boolean deleteNote(@RequestBody Map<String, String> info) {
        try{
            long userId=Long.parseLong(info.get("userId"));
            long dirId=Long.parseLong(info.get("dirId"));
            long noteId=Long.parseLong(info.get("noteId"));

            Directory dir=directoryRepository.findByIdAndUser(dirId, userId);
            Note note=noteRepository.findByIdAndUserId(noteId, userId);
            dir.getNotes().remove(note);
            directoryRepository.save(dir);

            noteRepository.deleteById(noteId);

            return true;
        } catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }

    }
}
