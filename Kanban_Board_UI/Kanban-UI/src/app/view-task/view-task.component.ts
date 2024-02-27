import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditTaskComponent } from 'src/app/edit-task/edit-task.component';
import { KanbanService } from '../services/kanban.service';
import { TokenInterceptorService } from '../services/token-interceptor.service';

@Component({
  selector: 'app-view-task',
  templateUrl: './view-task.component.html',
  styleUrls: ['./view-task.component.css']
})
export class ViewTaskComponent implements OnInit {
  @Input() task: any; 
  @Input() stageId: string;
  @Input() stageName: string;
  @Input() boardId: string;
  @Input() stages: any;
  @Input() members:[];
  @Input() creator;
  @Output() taskMoved: EventEmitter<any> = new EventEmitter();

  showCard: boolean = false;
  delete: boolean = false;
  userName: string = "";

  constructor(private kanbanService : KanbanService , private dialog: MatDialog, private tokenInterceptorService: TokenInterceptorService) { }

  ngOnInit(): void {
    this.userName = this.tokenInterceptorService.getUserName();
  }

  movetask(task,movetostage,movetostageName,currentstage,board){
    console.log(movetostage)
    this.kanbanService.moveTask(task,movetostage,currentstage,board).subscribe((response:any)=>{
    let data= response;
    data.taskMoved=true;
    this.taskMoved.emit(data); 
    let message = "Successfully moved from " + this.stageName + " to " + movetostageName; 
    this.kanbanService.showSuccess(message)     
     }); 
    }
    deleteTask(task,currentstage,board) {
      const confirmation = window.confirm('Are you sure you want to delete this task?');

      if (confirmation) {
        this.kanbanService.deleteTask(task, currentstage, board).subscribe((response: any) => {
          this.kanbanService.getBoard(board).subscribe((getResponse: any) => {
            getResponse.taskDeleted = true;
            this.taskMoved.emit(getResponse.body);
            let message="Deleted Successfully"; 
            this.kanbanService.showSuccess(message)  
          });
        });
      }
     
    
    }

    toggleCard(t){
      t.showCard = !t.showCard;
    }

    updateTask(task){
      let taskId=task.taskId;
      const modalResponse=this.dialog.open(EditTaskComponent, {
        disableClose: true,
        panelClass: 'custom-modal-class',
        data: {
          boardId: this.boardId,
          stageId:this.stageId,
          task:task,
          members:this.members,
          creator:this.creator
        },
      });
      modalResponse.afterClosed().subscribe((result: any) => {
        if(result) {
          for (const stage of result.stages) {
            if (stage.stageId === this.stageId) {
              this.task = stage.tasks;
              let message="Updated Successfully"; 
              this.kanbanService.showSuccess(message) 
              break;
            }
          }
        } else {
          console.log('Modal closed without a result');
        }
    });
    }


    getBackgroundColor(priority: string): string {
      switch (priority.toLowerCase()) {
        case 'high':
          return 'lightcoral';
        case 'normal':
          return 'rgb(198, 131, 215)'; 
        case 'low':
          return 'rgb(248, 255, 149)'; 
        default:
          return 'white'; 
      }
    }
  
}

