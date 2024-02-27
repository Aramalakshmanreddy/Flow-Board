import { Component, Inject } from '@angular/core';
import { Board } from '../models/board';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KanbanService } from '../services/kanban.service';

@Component({
  selector: 'app-new-board',
  templateUrl: './new-board.component.html',
  styleUrls: ['./new-board.component.css']
})
export class NewBoardComponent {

  board: Board = {};
  usedNumbers: Set<number> = new Set<number>();
  boardForm = this.fb.group({
    boardId: [null],
    boardName: ['',[Validators.required, Validators.pattern(/^[a-zA-Z][a-zA-Z0-9 .\-_]{2,}$/)]],
    description: ['',[Validators.required]],
    creationDate: [new Date()],
    membersRef: ['']
  });

  constructor(private fb: FormBuilder , private kanbanService : KanbanService, private dialogRef: MatDialogRef<NewBoardComponent>) {}

  get boardId() {return this.boardForm.get('boardId');}
  get boardName() {return this.boardForm.get('boardName');}
  get description() {return this.boardForm.get('description');}
  get creationDate() {return this.boardForm.get('creationDate');}
  get membersRef() {return this.boardForm.get('membersRef');}

  
 generateUniqueId(): number {
  let randomNum: number;
  do {
    randomNum = Math.floor(Math.random() * (100 - 1 + 1)) + 1;  //Math.floor(Math.random() * (max-min+1)+min)
  } while (this.usedNumbers.has(randomNum));

  this.usedNumbers.add(randomNum);
  return randomNum;
}

  createBoard() {
    let formData: Board = this.boardForm.value as Board;
    formData.boardId = this.generateUniqueId();
    formData.members = formData.membersRef?.split(',').map(member => member.trim());
  
    this.board.members  = this.board.membersRef?.split(',').map(member => member.trim());
    const creatorName = this.board.creator;
    
    const uniqueName = [...new Set(this.board.members )];
    this.board.members = uniqueName;
    if(!this.board.members.includes(creatorName)){
      this.board.members.push(creatorName);
    }
   
    console.log(formData);
    this.kanbanService.saveBoard(formData).subscribe((data:any)=>{
      let message="Board Created Successfully"; 
      this.kanbanService.showSuccess(message)
      this.dialogRef.close(data.body);
    })
  }

  closeModal(){
    this.dialogRef.close();
   }
}
