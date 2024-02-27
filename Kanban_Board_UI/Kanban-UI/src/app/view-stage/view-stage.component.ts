import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditStageComponent } from 'src/app/edit-stage/edit-stage.component';
import { KanbanService } from '../services/kanban.service';
import { TokenInterceptorService } from '../services/token-interceptor.service';

@Component({
  selector: 'app-view-stage',
  templateUrl: './view-stage.component.html',
  styleUrls: ['./view-stage.component.css']
})
export class ViewStageComponent implements OnInit{
  @Input() stages: any; 
  @Input() boardId: string;
  @Input() members: [];
  @Input() creator;
  @Output() taskMoved: EventEmitter<any> = new EventEmitter();

  userName: string = "";

constructor(private kanbanService: KanbanService , private dialog: MatDialog, private tokenInterceptorService: TokenInterceptorService){}


handelUpdatesStages(event){
  this.stages=event.stages;   
}
ngOnInit(): void {
  this.userName = this.tokenInterceptorService.getUserName();
  }

  deleteStage(boardId, stageId){
    const confirmation = window.confirm('Are you sure you want to delete this task?');

    if (confirmation) {
       this.kanbanService.deleteStage(boardId,stageId).subscribe((data:any)=>{
        this.kanbanService.getBoard(boardId).subscribe((getResponse: any) => {
          getResponse.taskDeleted = true;
           this.taskMoved.emit(getResponse.body);
           let message="Deleted Successfully"; 
            this.kanbanService.showSuccess(message) 
        });
        console.log("deleted");
      })
  }
}

updateStage(boardId , column){
  let stageId=this.stages.stageId
  const modalResponse=this.dialog.open(EditStageComponent, {
    disableClose: true,
    // width: '600px', 
    // height: '600px',
    data: {
      boardId: this.boardId,
      stage:column
    },
  });
  modalResponse.afterClosed().subscribe((result: any) => {
    if(result.stages) {
        this.stages = result.stages;
        let message="Updated Successfully"; 
        this.kanbanService.showSuccess(message) 
      
  } else {
    console.log('Modal closed without a result');
  }
});
}

}
