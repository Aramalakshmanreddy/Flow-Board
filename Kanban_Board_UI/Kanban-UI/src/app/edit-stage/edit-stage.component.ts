import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Stage } from '../models/stage';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { KanbanService } from '../services/kanban.service';

@Component({
  selector: 'app-edit-stage',
  templateUrl: './edit-stage.component.html',
  styleUrls: ['./edit-stage.component.css']
})
export class EditStageComponent {
  originalStage: Stage = {};

  constructor(private activatedRoute: ActivatedRoute, @Inject(MAT_DIALOG_DATA) public data: { boardId: number , stage:any } ,
  private dialogRef: MatDialogRef<EditStageComponent>, private kanbanService: KanbanService) { }

  stage: Stage = {};
  id!: number;

  ngOnInit(): void {
    this.originalStage = { ...this.data.stage };  // Create a copy of the original stage data

    this.stage = { ...this.data.stage };  // Use the copy for editing
  }

  editStage() {
    const stage={
      'stageId':this.stage.stageId,
      'stageName':this.stage.stageName
    }   
      this.kanbanService.updateStage(this.data.boardId, stage).subscribe((data:any)=>{
         this.dialogRef.close(data.body);
      })
  }

  closeModal(){
    this.stage = { ...this.originalStage };
    this.dialogRef.close();
   }
}
