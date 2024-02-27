package com.project.kanbanService.service;

import com.project.kanbanService.domain.Board;
import com.project.kanbanService.domain.Stage;
import com.project.kanbanService.domain.Task;
import com.project.kanbanService.domain.User;
import com.project.kanbanService.exception.*;
import com.project.kanbanService.proxy.AuthenticationProxy;
import com.project.kanbanService.proxy.EmailProxy;
import com.project.kanbanService.proxy.NotificationProxy;
import com.project.kanbanService.repository.KanbanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.util.ReflectionUtils.isNullable;
import static org.springframework.data.util.ReflectionUtils.isVoid;

@Service
public class KanbanServiceImpl implements IKanbanService {

    private final KanbanRepository kanbanRepository;
    private NotificationProxy notificationProxy;
    private AuthenticationProxy authenticationProxy;
    private EmailProxy emailProxy;

    @Autowired
    public KanbanServiceImpl(KanbanRepository kanbanRepository, NotificationProxy notificationProxy, AuthenticationProxy authenticationProxy, EmailProxy emailProxy) {
        this.kanbanRepository = kanbanRepository;
        this.notificationProxy = notificationProxy;
        this.authenticationProxy = authenticationProxy;
        this.emailProxy = emailProxy;
    }

    public Board getBoardFromUser(User user, int boardId) throws BoardNotFoundException
    {
        if(user.getBoards() == null || user.getBoards().isEmpty())
            throw new BoardNotFoundException();
        for (Board board : user.getBoards()) {
            if(boardId == board.getBoardId())
                return board;
        }
        throw new BoardNotFoundException();
    }
    public Stage getStageFromBoard(Board board, int stageId) throws StageNotFoundException{
        if(board.getStages() == null || board.getStages().isEmpty())
            throw new StageNotFoundException();
        for(Stage stage: board.getStages()) {
            if(stage.getStageId() == stageId)
                return stage;
        }
        throw new StageNotFoundException();
    }
    public Board createNewBoard(Board board) {
        Stage stage1 = new Stage(1, "To Be Done");
        Stage stage2 = new Stage(2, "In Progress");
        Stage stage3 = new Stage(3, "Backlog");
        Stage stage4 = new Stage(4, "Completed");
        board.setStages(Arrays.asList(stage1, stage2, stage3, stage4));
        return board;
    }
    public void giveMemberAccess(String[] members, String email, String creator,Board board) {
        Set<String> uniqueNames = new HashSet<>();
        for (String memberName : members) {
            if (memberName.isEmpty() || creator.equalsIgnoreCase(memberName))
                continue;
            if (uniqueNames.add(memberName)) {
                List<Board> userBoardList=new ArrayList<>();
                User getuser = kanbanRepository.findByUserName(memberName);
                if(getuser==null) {
                    User member = new User();
                    member.setUserName(memberName);
                    String memberEmail = memberName.concat("@gmail.com");
                    member.setEmail(memberEmail);
                    getuser = authenticationProxy.saveUserInMysql(member);
                    userBoardList.add(board);
                }else{
                    List<Board> getBoardList=getuser.getBoards();
                    if (getBoardList == null || getBoardList.isEmpty()) {
                        getBoardList = new ArrayList<>();
                        getBoardList.add(board);
                    }else{
                        getBoardList.add(board);
                    }
                    userBoardList.addAll(getBoardList);
                }
                getuser.setBoards(userBoardList);
                System.out.println("saveduser"+getuser);
                kanbanRepository.save(getuser);

            }
        }
    }
    //  public void editMembersAccess(Board board, Board updatedBoard, User user) {
//        giveMemberAccess(updatedBoard.getMembers(), user.getEmail(),board.getCreator(),updatedBoard);

//        Set<String> updatedMembersSet = new HashSet<>(List.of(updatedBoard.getMembers()));
//        for (String member : board.getMembers()) {
//            if (member.isEmpty())
//                continue;
//            if (!updatedMembersSet.contains(member) && !member.equals(user.getUserName())) {
//                User removeUser = new User();
//                removeUser.setUserName(member);
//                removeUser.setCreatorEmail(user.getCreatorEmail());
//                authenticationProxy.deleteUserInMysql(removeUser);
//            }
//        }
 //   }
    @Override
    public User saveUser(User user) throws UserAlreadyExistsException {
        if(kanbanRepository.findById(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException();
        System.out.println("save user with board"+user);
        return kanbanRepository.save(user);
    }

    @Override
    public Board saveBoard(Board board, String email) throws BoardAlreadyExistsException, UserNotFoundException, UserAlreadyExistsException {

        board = createNewBoard(board);
        board.setCreatorEmail(email);
        if(kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();

        User user = kanbanRepository.findById(email).get();

        if(user.getBoards() == null)
            user.setBoards(Arrays.asList(board));
        else {
            List<Board> boardList=user.getBoards();
            System.out.println("boardlist"+boardList);
//            for (Board board1 : boardList) {
//                if(board1.getBoardId() == board.getBoardId())
//                    throw new BoardAlreadyExistsException();
//            }
            boardList.add(board);
            user.setBoards(boardList);
        }

        System.out.println("save board"+user);
        kanbanRepository.save(user);
        List<Board> saveBoardToMember =new ArrayList<>();
        saveBoardToMember.add(board);
        if (board.getMembers().length >= 1)
            giveMemberAccess(board.getMembers(), email,board.getCreator(),board);
        return board;
    }

    @Override
    public Board saveStage(Stage stage, int boardId, String email) throws StageAlreadyExistsException, BoardNotFoundException, UserNotFoundException {
        if(kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();

        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, boardId);
        String[] boardUsers = board.getMembers();
        List<Board> boardList=new ArrayList<>();
        for(String member : boardUsers) {
            User getuser = kanbanRepository.findByUserName(member);
            Board getboard = getBoardFromUser(getuser, boardId);
            boardList.add(getboard);
            if (getboard.getStages() == null)
                getboard.setStages(Arrays.asList(stage));
            else {
                List<Stage> stageList = getboard.getStages();
                for (Stage stage1 : stageList) {
                    if (stage1.getStageId() == stage.getStageId())
                        throw new StageAlreadyExistsException();
                }
                stageList.add(stage);
                getboard.setStages(stageList);
            }
            kanbanRepository.save(getuser);
        }
        for(Board boards:boardList){
            if(boards.getCreator().equalsIgnoreCase(user.getUserName())){
                return boards;
            }
        }
        return null;
    }

    @Override
    public Board saveTask(Task task, int boardId, int stageId, String email) throws TaskAlreadyExistsException, UserNotFoundException,  BoardNotFoundException, StageNotFoundException {
        if(kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();

        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, boardId);
        String[] boardUsers = board.getMembers();
        System.out.println("boardUsers"+boardUsers);
        List<Board> boardList=new ArrayList<>();
        for(String member : boardUsers) {
            System.out.println("member"+member);
            User getuser = kanbanRepository.findByUserName(member);
            System.out.println("getuser"+getuser);
            Board getboard = getBoardFromUser(getuser, boardId);
            boardList.add(getboard);
            System.out.println("getboard"+getboard);
            Stage stage = getStageFromBoard(getboard, stageId);
            System.out.println("stage"+stage);
            if (stage.getTasks() == null){
                System.out.println("No stage Tasks");
                stage.setTasks(Arrays.asList(task));}
            else {
                List<Task> taskList = stage.getTasks();
                for (Task task1 : taskList) {
                    if (task1.getTaskId() == task.getTaskId())
                        throw new TaskAlreadyExistsException();
                }
                taskList.add(task);
                stage.setTasks(taskList);
            }
            System.out.println("stage Task"+stage.getTasks());
            System.out.println(" Task getuser"+getuser);
            kanbanRepository.save(getuser);
        }
        for(Board boards:boardList){
            if(boards.getCreator().equalsIgnoreCase(user.getUserName())){
                return boards;
            }
        }
        return null;
    }

    @Override
    public User getUser(String email) throws UserNotFoundException {
        return kanbanRepository.findById(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Board getBoard(int boardId, String email) throws BoardNotFoundException, UserNotFoundException {
        User user =  getUser(email);
        return getBoardFromUser(user, boardId);
    }

    @Override
    public Task getTask(int taskId, int stageId, int boardId, String email) throws TaskNotFoundException, UserNotFoundException, BoardNotFoundException, StageNotFoundException {
        Board board = getBoard(boardId, email);
        Stage stage = getStageFromBoard(board , stageId);

        if (stage.getTasks() == null || stage.getTasks().isEmpty())
            throw new TaskNotFoundException();

        List<Task> tasKList = stage.getTasks();
        for(Task task : tasKList) {
            if(task.getTaskId() == taskId)
                return task;
        }
        throw new TaskNotFoundException();
    }

    @Override
    public User deleteBoard(int boardId, String email) throws BoardNotFoundException, UserNotFoundException {
        boolean boardDeleted = false;
        System.out.println(email);
        if (kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();

        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, boardId);
        String[] boardUsers = board.getMembers();
        List<String> boardUsersList = new ArrayList<>(Arrays.asList(boardUsers));
        boolean loginUser = false;
        for (String member : boardUsersList) {
            if (member.equalsIgnoreCase(user.getUserName())) {
                loginUser = true;
                break;
            }
        }
        if (!loginUser) {
            boardUsersList.add(user.getUserName());
            boardUsers = boardUsersList.toArray(new String[0]);
        }
        for(String member : boardUsers) {

            User getuser = kanbanRepository.findByUserName(member);
            if (getuser.getBoards() != null || !getuser.getBoards().isEmpty()) {
                List<Board> boardList = getuser.getBoards();
                Iterator<Board> boardIterator = boardList.iterator();
                while (boardIterator.hasNext()) {
                    if (boardIterator.next().getBoardId() == boardId) {
                        boardIterator.remove();
                        boardDeleted = true;
                    }
                }
//                if (!boardDeleted)
//                    throw new BoardNotFoundException();
                kanbanRepository.save(getuser);
            }
        }
        return kanbanRepository.findById(email).get();
    }

    @Override
    public Board deleteStage(int stageId, int boardId, String email) throws StageNotFoundException, UserNotFoundException, BoardNotFoundException {
        boolean stageDeleted = false;
        if(kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();

        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, boardId);
        String[] boardUsers = board.getMembers();
        List<Board> boardList=new ArrayList<>();
        for(String member : boardUsers) {
            User getuser = kanbanRepository.findByUserName(member);
            Board getboard = getBoardFromUser(getuser, boardId);
            boardList.add(getboard);
            if (getboard.getStages() == null || getboard.getStages().isEmpty())
                throw new StageNotFoundException();

            List<Stage> stageList = getboard.getStages();
            Iterator<Stage> stageIterator = stageList.iterator();
            while (stageIterator.hasNext()) {
                if (stageIterator.next().getStageId() == stageId) {
                    stageIterator.remove();
                    stageDeleted = true;
                }
            }
            if (!stageDeleted)
                throw new StageNotFoundException();

            kanbanRepository.save(getuser);
        }
        for(Board boards:boardList){
            if(boards.getCreator().equalsIgnoreCase(user.getUserName())){
                return boards;
            }
        }
        return null;
    }

    @Override
    public Board deleteTask(int taskId, int stageId, int boardId, String email) throws TaskNotFoundException, UserNotFoundException, BoardNotFoundException, StageNotFoundException {
        boolean taskDeleted = false;
        if(kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();
        List<Board> boardList=new ArrayList<>();
        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, boardId);
        String[] boardUsers = board.getMembers();
        for(String member : boardUsers) {
            User getuser = kanbanRepository.findByUserName(member);
            Board getboard = getBoardFromUser(getuser, boardId);
            boardList.add(getboard);
            Stage stage = getStageFromBoard(getboard, stageId);
            if (stage.getTasks() == null || stage.getTasks().isEmpty())
                throw new TaskNotFoundException();

            List<Task> taskList = stage.getTasks();
            Iterator<Task> taskIterator = taskList.iterator();
            while (taskIterator.hasNext()) {
                if (taskIterator.next().getTaskId() == taskId) {
                    taskIterator.remove();
                    taskDeleted = true;
                }
            }
            if (!taskDeleted)
                throw new StageNotFoundException();

            kanbanRepository.save(getuser);
        }
        for(Board boards:boardList){
            if(boards.getCreator().equalsIgnoreCase(user.getUserName())){
                return boards;
            }
        }
        return null;
    }

    @Override
    public Board updateBoard(Board updatedBoard, String email) throws BoardNotFoundException, UserNotFoundException {
        boolean boardUpdated = false;
        if(kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();
        List<Board> bl=new ArrayList<>();
        bl.add(updatedBoard);
        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, updatedBoard.getBoardId());
        String[] boardUsers = board.getMembers();
        String[] oldUsersUpdated =  updateBoardForUser(boardUsers, updatedBoard.getMembers());
        String[] newUsersUpdated =  updateBoardForUser(updatedBoard.getMembers(), boardUsers);
        if(newUsersUpdated.length>0){
            giveMemberAccess(newUsersUpdated, email,board.getCreator(),updatedBoard);
        }
        if(oldUsersUpdated.length>0){
            deleteBoardFromMember(oldUsersUpdated,updatedBoard);
        }
        board = getBoardFromUser(user, updatedBoard.getBoardId());
        System.out.println("board"+board);
        boardUsers = board.getMembers();
        List<Board> boardsList=new ArrayList<>();
        for(String member : boardUsers) {
            if(member!=null || !member.isEmpty() || member!="") {
                User getuser = kanbanRepository.findByUserName(member);
                if(getuser!=null) {
                    if (getuser.getBoards() != null || !getuser.getBoards().isEmpty()){
                    List<Board> boardList = getuser.getBoards();
                    for (int i = 0; i < boardList.size(); i++) {
                        Board boards = boardList.get(i);
                        if (boards.getBoardId() == updatedBoard.getBoardId()) {
                            updatedBoard.setCreator(boards.getCreator());
                            updatedBoard.setCreationDate(new Date());
                            updatedBoard.setStages(boards.getStages());
                            if (updatedBoard.getMembers() != null && updatedBoard.getMembers().length >= 1) {
                                // editMembersAccess(boards, updatedBoard, user);
                            }
                            boardList.set(i, updatedBoard);
                            boardsList.add(updatedBoard);
                            boardUpdated = true;
                            break;
                        }
                    }

//                    if (!boardUpdated)
//                        throw new BoardNotFoundException();

                    kanbanRepository.save(getuser);
                }
                }
            }
        }
        for(Board boards:boardsList){
            if(boards.getCreator().equalsIgnoreCase(user.getUserName())){
                return boards;
            }
        }
        return board;
    }
    public String[] updateBoardForUser(String[] oldMembers, String[] newMembers){
        Set<String> set1 = new HashSet<>(Arrays.asList(oldMembers));
        Set<String> set2 = new HashSet<>(Arrays.asList(newMembers));
        Set<String> extraNames = new HashSet<>(set1);
        extraNames.removeAll(set2);
        return extraNames.toArray(new String[0]);
    }
    public void deleteBoardFromMember(String[] boardUsers,Board updatedBoard) throws BoardNotFoundException {
        boolean boardDeleted = false;
        for (String member : boardUsers) {
            User getuser = kanbanRepository.findByUserName(member);
            if(getuser!=null) {
                if (getuser.getBoards() != null || !getuser.getBoards().isEmpty()) {
                    List<Board> boardList = getuser.getBoards();
                    Iterator<Board> boardIterator = boardList.iterator();
                    while (boardIterator.hasNext()) {
                        if (boardIterator.next().getBoardId() == updatedBoard.getBoardId()) {
                            boardIterator.remove();
                            boardDeleted = true;
                        }
                    }
                    if (!boardDeleted)
                        throw new BoardNotFoundException();
                    kanbanRepository.save(getuser);
                }
            }
        }
    }
    @Override
    public Board updateStage(Stage updatedStage, int boardId, String email) throws StageNotFoundException, UserNotFoundException, BoardNotFoundException {
        boolean stageUpdated = false;
        if (kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();

        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, boardId);
        String[] boardUsers = board.getMembers();
        List<Board> boardList=new ArrayList<>();
        for(String member : boardUsers){
            User getuser = kanbanRepository.findByUserName(member);
            Board getboard = getBoardFromUser(getuser, boardId);
            boardList.add(getboard);
        if (getboard.getStages() == null || getboard.getStages().isEmpty())
            throw new StageNotFoundException();

        List<Stage> stageList = getboard.getStages();
        for (int i = 0; i < stageList.size(); i++) {
            Stage stage = stageList.get(i);
            if (stage.getStageId() == updatedStage.getStageId()) {
                updatedStage.setTasks(stage.getTasks());
                stageList.set(i, updatedStage);
                stageUpdated = true;
                break;
            }
        }
        if (!stageUpdated)
            throw new StageNotFoundException();

        kanbanRepository.save(getuser);
    }
        for(Board boards:boardList){
            if(boards.getCreator().equalsIgnoreCase(user.getUserName())){
                return boards;
            }
        }
        return null;
    }

    @Override
    public Board updateTask(Task updatedTask, int stageId, int boardId, String email) throws TaskNotFoundException, UserNotFoundException, BoardNotFoundException, StageNotFoundException {
        boolean taskUpdated = false;
        if(kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();
        List<Board> boardList=new ArrayList<>();
        User user = kanbanRepository.findById(email).get();
        Board board = getBoardFromUser(user, boardId);
        String[] boardUsers = board.getMembers();
        for(String member : boardUsers){
            User getuser = kanbanRepository.findByUserName(member);
            Board getboard = getBoardFromUser(getuser, boardId);
            boardList.add(getboard);
            Stage stage = getStageFromBoard(getboard, stageId);
            if(stage.getTasks()==null || stage.getTasks().isEmpty())
                throw new TaskNotFoundException();

            List<Task> taskList = stage.getTasks();
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                if (task.getTaskId() == updatedTask.getTaskId()) {
                    taskList.set(i, updatedTask);
                    taskUpdated = true;
                    break;
                }
            }
            if(!taskUpdated)
                throw new StageNotFoundException();

            kanbanRepository.save(getuser);
        }
        for(Board boards:boardList){
            if(boards.getCreator().equalsIgnoreCase(user.getUserName())){
                return boards;
            }
        }

        return null;
    }
    public void saveUserWhenUpdated(User user){
        kanbanRepository.save(user);
    }
    @Override
    public Board moveTask(int taskId, int boardId, int currentStageId, int newStageId, String email) throws WrongMoveException, DataMissingException, UserNotFoundException, StageNotFoundException, TaskNotFoundException, BoardNotFoundException, TaskAlreadyExistsException {
        Task taskToMove = getTask(taskId, currentStageId, boardId, email);
        String assignedTo = taskToMove.getAssignee();

        if(assignedTo == null)
            throw new DataMissingException();

        if (kanbanRepository.findById(email).isEmpty())
            throw new UserNotFoundException();

        User user = kanbanRepository.findById(email).get();
        if(newStageId == 2){
            Board board = getBoardFromUser(user, boardId);
            Stage stage = getStageFromBoard(board, 2);  // stageId = 2 is for in-progress stage

            if (stage.getTasks() == null)
                stage.setTasks(new ArrayList<Task>());
            else {
                List<Task> taskList = stage.getTasks();
                int count = 0;
                for(Task task: taskList){
                    if(task.getAssignee().equals(assignedTo))
                        count++;
                }
                if(count >= 2)
                    throw new WrongMoveException();
            }
        }
        saveTask(taskToMove, boardId, newStageId, email);
        deleteTask(taskId, currentStageId, boardId, email);

        if (newStageId == 4)   {        // stageId = 4 is for completed stage
            String message = notificationProxy.saveNotification(taskToMove);
            System.out.println("Notification: "+ message);
            String taskMessage = taskToMove.getTaskName().concat(" task completed by ").concat(taskToMove.getAssignee());
            emailProxy.completedTaskConfirmation(email, taskMessage);
        }

        return getBoard(boardId, email);
    }
}
