import { Component } from '@angular/core';
import { Stage } from '../models/stage';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Inject } from '@angular/core';
import { KanbanService } from '../services/kanban.service';
@Component({
  selector: 'app-new-stage',
  templateUrl: './new-stage.component.html',
  styleUrls: ['./new-stage.component.css']
})
export class NewStageComponent {

  stage: Stage = {};
  usedNumbers: Set<number> = new Set<number>();
  stageForm = this.fb.group({
    stageId: [null],
    stageName: ['',[Validators.required, Validators.pattern(/^[a-zA-Z][a-zA-Z0-9 .\-_]{2,}$/)]],
  });

  constructor(private fb: FormBuilder , private kanbanService: KanbanService , @Inject(MAT_DIALOG_DATA) public data: { boardId: number } , private dialogRef: MatDialogRef<NewStageComponent>) {}

  get stageId() {return this.stageForm.get('stageId');}
  get stageName() {return this.stageForm.get('stageName');}
  

  
generateUniqueId(): number {
  let randomNum: number;
  do {
    randomNum = Math.floor(Math.random() * (100 - 5 + 1)) + 5;  //Math.floor(Math.random() * (max-min+1)+min)
  } while (this.usedNumbers.has(randomNum));

  this.usedNumbers.add(randomNum);
  return randomNum;
}

  createStage() {
    let formData: Stage = this.stageForm.value as Stage;
    formData.stageId = this.generateUniqueId();
    console.log(formData);
    this.kanbanService.saveStage(this.data.boardId , formData).subscribe((data:any)=>{
      let message="Stage Created Successfully"; 
      this.kanbanService.showSuccess(message)
        this.dialogRef.close(data.body);
    });
  }

  closeModal(){
    this.dialogRef.close();
   }
}
