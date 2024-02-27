import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class KanbanService {
  constructor(private http: HttpClient,private snackBar: MatSnackBar) { }

  getBoardList() {
    return this.http.get(`http://localhost:9000/api/v2/user/fetch`, { observe: 'response' });
  }
  getBoard(bId) {
    return this.http.get(`http://localhost:9000/api/v2/board/fetch/boardId/${bId}`, { observe: 'response' });
  }


  deleteTask(taskId, stageId, boardId) {
    return this.http.delete(`http://localhost:9000/api/v2/task/delete/boardId/${boardId}/stageId/${stageId}/taskId/${taskId}`, {
      observe: 'response', withCredentials: true
    });
  }
  deleteBoard(boardId) {
    return this.http.delete(`http://localhost:9000/api/v2/board/delete/boardId/${boardId}`, { observe: `response`, withCredentials: true });
  }

  deleteStage(boardId, stageId) {
    return this.http.delete(`http://localhost:9000/api/v2/stage/delete/boardId/${boardId}//stageId/${stageId}`, { observe: `response`, withCredentials: true })
  }


  saveBoard(formData) {
    return this.http.post(`http://localhost:9000/api/v2/board/save`, formData, { observe: 'response' })
  }
  saveStage(boardId, formData) {
    return this.http.post(`http://localhost:9000/api/v2/stage/save/boardId/${boardId}`, formData, { observe: 'response' });

  }
  saveTask(boardId: number, taskInput) {
    return this.http.post(`http://localhost:9000/api/v2/task/save/boardId/${boardId}/stageId/1`, taskInput, {
      observe: 'response', withCredentials: true
    });
  }

  updateBoard(board: any) {
    return this.http.put(`http://localhost:9000/api/v2/board/update`, board, { observe: 'response' });
  }
  updateStage(boardId, stage) {
    return this.http.put(`http://localhost:9000/api/v2/stage/update/boardId/${boardId}`, stage, { observe: `response` })
  }
  updateTask(boardId, stageId, task) {
    return this.http.put(`http://localhost:9000/api/v2/stage/update/boardId/${boardId}/stage/${stageId}`, task, { observe: `response` })
  }


  moveTask(taskId, newStageId, currentStageId, boardId) {
    return this.http.post(`http://localhost:9000/api/v2/task/move/from/${currentStageId}/to/${newStageId}/taskId/${taskId}/boardId/${boardId}`, {
      observe: 'response', withCredentials: true
    });
  }
   
  public showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar'],
    });
}
}
