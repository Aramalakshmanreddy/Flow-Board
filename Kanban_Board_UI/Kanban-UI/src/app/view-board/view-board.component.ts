import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';

import { NewStageComponent } from 'src/app/new-stage/new-stage.component';
import { NewTaskComponent } from 'src/app/new-task/new-task.component';
import { EditBoardComponent } from 'src/app/edit-board/edit-board.component';
import { KanbanService } from '../services/kanban.service';
import { TokenInterceptorService } from '../services/token-interceptor.service';


@Component({
  selector: 'app-view-board',
  templateUrl: './view-board.component.html',
  styleUrls: ['./view-board.component.css']
})
export class ViewBoardComponent implements OnInit{
  @Output() dataUpdated: EventEmitter<any> = new EventEmitter();
  @Input() viewBoard:any

  userName:String = "";

  constructor(private kanbanService:KanbanService, private dialog: MatDialog, private tokenInterceptorService:TokenInterceptorService){
   
    }
  @Output() data: EventEmitter<any> = new EventEmitter();
  
  ngOnInit(): void {
    this.userName = this.tokenInterceptorService.getUserName();
  }
  newTask(){

    const modalResponse=this.dialog.open(NewTaskComponent, {
      disableClose: true,
      data: {
        boardId: this.viewBoard.boardId,
        members:this.viewBoard.members,
        creator: this.viewBoard.creator
      },
    });
    modalResponse.afterClosed().subscribe((result: any) => {
      if(result) {
        this.viewBoard=result
      } else {
        console.log('Modal closed without a result');
      }
  });
}

newStage(){
  const modalResponse=this.dialog.open(NewStageComponent, {
    disableClose: true,
    data: {
      boardId: this.viewBoard.boardId,
    },
  });
  modalResponse.afterClosed().subscribe((result: any) => {
    if(result) {
      this.viewBoard=result
    } else {
      console.log('Modal closed without a result');
    }
});
}

handelUpdatesStages(event){
  this.viewBoard=event
  }
  

  updateBoard(boardId){
    const modalResponse=this.dialog.open(EditBoardComponent, {
      disableClose: true,
      data: {
          board : this.viewBoard
      },
    });
    modalResponse.afterClosed().subscribe((result: any) => {
      if(result) {
        this.viewBoard=result;
        result.boardUpdated=true;
        this.dataUpdated.emit(result);
        let message="Updated Successfully"; 
        this.kanbanService.showSuccess(message) 
      } else {
        console.log('Modal closed without a result');
      }
    
  });
  }
  deleteBoard(list)
{
  const confirmation = window.confirm('Are you sure you want to delete this task?');
  if (confirmation) {
    this.kanbanService.deleteBoard(list.boardId).subscribe((data:any)=>
    {
      if(data.status==204){
      data.boardDeleted=true;
      this.dataUpdated.emit(data);
      let message="Deleted Successfully"; 
      this.kanbanService.showSuccess(message)
    }
    });
}
}
}
  



