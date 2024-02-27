package com.project.kanbanService.service;

import com.project.kanbanService.domain.Board;
import com.project.kanbanService.domain.Stage;
import com.project.kanbanService.domain.Task;
import com.project.kanbanService.domain.User;
import com.project.kanbanService.exception.*;

public interface IKanbanService {

    User saveUser(User user)throws UserAlreadyExistsException;
    Board saveBoard(Board board, String email) throws BoardAlreadyExistsException, UserNotFoundException, UserAlreadyExistsException;
    // For Saving Stage, we need to identify board with boardId
    Board saveStage(Stage stage, int boardId, String email)throws StageAlreadyExistsException, BoardNotFoundException, UserNotFoundException;
    //For Saving task , we need to identify board and stage with specific id's
    Board saveTask(Task task, int boardId, int stageId, String email)throws TaskAlreadyExistsException, UserNotFoundException,  BoardNotFoundException, StageNotFoundException;

    User getUser(String email)throws UserNotFoundException;
    Board getBoard(int boardId, String email) throws BoardNotFoundException, UserNotFoundException;
    Task getTask(int taskId, int stageId, int boardId, String email)throws TaskNotFoundException, BoardNotFoundException, UserNotFoundException, StageNotFoundException;

    User deleteBoard(int boardId, String email)throws BoardNotFoundException, UserNotFoundException;
    Board deleteStage(int stageId, int boardId, String email) throws StageNotFoundException, BoardNotFoundException, UserNotFoundException;
    Board deleteTask(int taskId, int stageId, int boardId, String email) throws TaskNotFoundException, BoardNotFoundException, StageNotFoundException, UserNotFoundException;

    Board updateBoard(Board updatedBoard, String email) throws BoardNotFoundException, UserNotFoundException, UserAlreadyExistsException;
    Board updateStage(Stage updatedStage, int boardId, String email)throws StageNotFoundException, BoardNotFoundException, UserNotFoundException;
    Board updateTask(Task updatedTask,int stageId, int boardId, String email)throws TaskNotFoundException, StageNotFoundException, BoardNotFoundException, UserNotFoundException;

    Board moveTask(int taskId, int boardId, int currentStageId, int newStageId, String email) throws WrongMoveException, UserNotFoundException, StageNotFoundException, TaskNotFoundException, BoardNotFoundException, TaskAlreadyExistsException, DataMissingException;

    //get the task object from the getTask method(taskId,currentStageId,boardId)
    //save the task in new stage using saveTask method(task, boardId, newStageId)
    //delete the task in current stage using deleteTask method(taskId, boardId, currentStageId)

}
