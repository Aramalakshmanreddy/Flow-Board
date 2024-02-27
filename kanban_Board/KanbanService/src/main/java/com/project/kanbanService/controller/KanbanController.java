package com.project.kanbanService.controller;

import com.project.kanbanService.domain.Board;
import com.project.kanbanService.domain.Stage;
import com.project.kanbanService.domain.Task;
import com.project.kanbanService.domain.User;
import com.project.kanbanService.exception.*;
import com.project.kanbanService.service.IKanbanService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/api/v2")
public class KanbanController {
    private final IKanbanService kanbanService;
    private ResponseEntity responseEntity;
    @Autowired
    public KanbanController(IKanbanService kanbanService) {
        this.kanbanService = kanbanService;
    }
    private String getEmailFromToken(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        return claims.getSubject();
    }

    //create methods
    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@RequestBody User user, HttpServletRequest request) throws UserAlreadyExistsException {
        try {
            responseEntity = new ResponseEntity<>(kanbanService.saveUser(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException exception) {
            throw new UserAlreadyExistsException();
        }
        return responseEntity;
    }
    @PostMapping("/board/save")
    public ResponseEntity<?> saveBoard(@RequestBody Board board, HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            String username = (String) claims.get("UserName");      //get the creator from claims instead of frontEnd
            board.setCreator(username);

            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.saveBoard(board, email), HttpStatus.CREATED);
        } catch (UserNotFoundException |BoardAlreadyExistsException | UserAlreadyExistsException exception) {
            throw new RuntimeException(exception);
        }
        return responseEntity;
    }
    @PostMapping("/stage/save/boardId/{boardId}")
    public ResponseEntity<?> saveStage(@RequestBody Stage stage, @PathVariable int boardId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.saveStage(stage, boardId, email), HttpStatus.CREATED);
        } catch (UserNotFoundException | BoardNotFoundException | StageAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
    @PostMapping("/task/save/boardId/{boardId}/stageId/{stageId}")
    public ResponseEntity<?> saveTask(@RequestBody Task task, @PathVariable int boardId, @PathVariable int stageId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.saveTask(task, boardId, stageId, email), HttpStatus.CREATED);
        } catch (UserNotFoundException | BoardNotFoundException | TaskAlreadyExistsException | StageNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }

    //get methods
    @GetMapping("/user/fetch")
    public ResponseEntity<?> fetchUser(HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.getUser(email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
    @GetMapping("/board/fetch/boardId/{boardId}")
    public ResponseEntity<?> fetchBoard(@PathVariable int boardId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.getBoard(boardId, email), HttpStatus.OK);
        } catch (UserNotFoundException | BoardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
    @GetMapping("/task/fetch/boardId/{boardId}/stageId/{stageId}/taskId/{taskId}")
    public ResponseEntity<?> fetchTask(@PathVariable int boardId, @PathVariable int stageId, @PathVariable int taskId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.getTask(taskId, stageId, boardId, email), HttpStatus.OK);
        } catch (UserNotFoundException | BoardNotFoundException | StageNotFoundException | TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }

    //delete methods
    @DeleteMapping("/board/delete/boardId/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable int boardId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.deleteBoard(boardId, email), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException | BoardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
    @DeleteMapping("/stage/delete/boardId/{boardId}/stageId/{stageId}")
    public ResponseEntity<?> deleteStage(@PathVariable int boardId, @PathVariable int stageId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.deleteStage(stageId, boardId, email), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException | BoardNotFoundException | StageNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
    @DeleteMapping("/task/delete/boardId/{boardId}/stageId/{stageId}/taskId/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable int boardId, @PathVariable int stageId,  @PathVariable int taskId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.deleteTask(taskId, stageId, boardId, email), HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException | BoardNotFoundException | StageNotFoundException | TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }

    //update methods
    @PutMapping("/board/update")
    public ResponseEntity<?> updateBoard(@RequestBody Board board, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.updateBoard(board, email), HttpStatus.ACCEPTED);
        } catch (UserNotFoundException | BoardNotFoundException | UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
    @PutMapping("/stage/update/boardId/{boardId}")
    public ResponseEntity<?> updateStage(@RequestBody Stage stage, @PathVariable int boardId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.updateStage(stage, boardId, email), HttpStatus.ACCEPTED);
        } catch (UserNotFoundException | BoardNotFoundException | StageNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
    @PutMapping("/stage/update/boardId/{boardId}/stage/{stageId}")
    public ResponseEntity<?> updateTask(@RequestBody Task task, @PathVariable int boardId, @PathVariable int stageId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.updateTask(task, stageId, boardId, email), HttpStatus.ACCEPTED);
        } catch (UserNotFoundException | BoardNotFoundException | StageNotFoundException | TaskNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }

    @PostMapping("/task/move/from/{currentStageId}/to/{newStageId}/taskId/{taskId}/boardId/{boardId}")
    public ResponseEntity<?> moveTask(@PathVariable int taskId, @PathVariable int boardId,@PathVariable int currentStageId, @PathVariable int newStageId, HttpServletRequest request) {
        try {
            String email = getEmailFromToken(request);
            responseEntity = new ResponseEntity<>(kanbanService.moveTask(taskId, boardId, currentStageId, newStageId, email), HttpStatus.ACCEPTED);
        } catch (WrongMoveException | UserNotFoundException | StageNotFoundException | TaskNotFoundException |
                 TaskAlreadyExistsException | DataMissingException | BoardNotFoundException e) {
            throw new RuntimeException(e);
        }
        return responseEntity;
    }
}
