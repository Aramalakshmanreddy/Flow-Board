import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { KanbanService } from '../services/kanban.service';

@Component({
  selector: 'app-board-list',
  templateUrl: './board-list.component.html',
  styleUrls: ['./board-list.component.css']
})
export class BoardListComponent implements OnInit{
  
@Output() dataUpdated: EventEmitter<any> = new EventEmitter();
@Input() boardListData;

 public boardlist:any
 public selectedBoardId;
 
 constructor(private fb:FormBuilder,
  private kanbanService:KanbanService,
  private router: Router){
 
  }
 ngOnInit(){
  if(this.boardListData)
    this.selectedBoardId=this.boardListData[0].boardId;
 }

public onSelection(list){
this.selectedBoardId=list.boardId;
this.kanbanService.getBoard(list.boardId).subscribe((data:any)=>{
  this.dataUpdated.emit(data.body);
})

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
    }
    });
}
}
}
