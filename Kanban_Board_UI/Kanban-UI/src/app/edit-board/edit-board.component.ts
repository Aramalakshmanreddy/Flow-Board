import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Board } from '../models/board';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KanbanService } from '../services/kanban.service';

@Component({
  selector: 'app-edit-board',
  templateUrl: './edit-board.component.html',
  styleUrls: ['./edit-board.component.css']
})
export class EditBoardComponent {
  originalBoard: Board = {};

  constructor(private activatedRoute: ActivatedRoute, @Inject(MAT_DIALOG_DATA) public data: { board : any } ,
  private dialogRef: MatDialogRef<EditBoardComponent>, private kanbanService: KanbanService) { }

  board: Board = {};
  id!: number;

  ngOnInit(): void {
    this.originalBoard = { ...this.data.board };  // Create a copy of the original board data

    this.board = { ...this.data.board };  // Use the copy for editing
    this.board.membersRef = this.getMembersList(this.board.members);
  }
  getMembersList(membersList: any): string{
    let memberRef: string = "";
    for(let member of membersList) {
      memberRef = memberRef.concat(member)+",";
    }
    return memberRef;
  }

  editBoard() {
    const board ={
      'boardId':this.data.board.boardId,
      'boardName':this.board.boardName,
      'description' : this.board.description,
      'members' : this.board.members
    }     
    this.board.members  = this.board.membersRef?.split(',').map(member => member.trim());
    const creatorName = this.board.creator;
    
    const uniqueName = [...new Set(this.board.members )];
    this.board.members = uniqueName;
    if(!this.board.members.includes(creatorName)){
      this.board.members.push(creatorName);
    }
    const updateBoard ={
      'boardId':this.data.board.boardId,
      'boardName':this.board.boardName,
      'description' : this.board.description,
      'members' : this.board.members
    }

    this.kanbanService.updateBoard(this.board).subscribe((data:any)=>{
      this.dialogRef.close(data.body);
    })

  }
  closeModal(){
    this.board = { ...this.originalBoard };
    this.dialogRef.close();
   }

}
