import { Component } from '@angular/core';
import { Task } from '../models/task';
import { FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Inject } from '@angular/core';
import { KanbanService } from '../services/kanban.service';
@Component({
  selector: 'app-new-task',
  templateUrl: './new-task.component.html',
  styleUrls: ['./new-task.component.css']
})
export class NewTaskComponent {

  constructor(private fb : FormBuilder, private dialogRef: MatDialogRef<NewTaskComponent> ,private kanbanService: KanbanService,
    @Inject(MAT_DIALOG_DATA) public data: { boardId: number , members: [] , creator: string }){};

  task : Task = {};
  minDate : Date = new Date();
  members: [] = this.data.members;
  creator :  String = this.data.creator;
  usedNumbers: Set<number> = new Set<number>();

  taskForm = this.fb.group({
    taskId: [null],
    taskName : ["",[Validators.required, Validators.pattern(/^[a-zA-Z][a-zA-Z0-9 .\-_]{2,}$/)]],
    description :["",[Validators.required]],
    assignee: ["",[Validators.required]],
    dueDate: ["",[Validators.required]],
    priority : ["Normal",[Validators.required]]
  });

    get taskId() {return this.taskForm.get('taskId');}
    get taskName() {return this.taskForm.get('taskName');}
    get description() {return this.taskForm.get('description');}
    get assignee() {return this.taskForm.get('assignee');}
    get dueDate() {return this.taskForm.get('dueDate');}
    get priority() {return this.taskForm.get('priority');}
    
    
    generateUniqueId(): number {
      let randomNum: number;
      do {
        randomNum = Math.floor(Math.random() * (100 - 1 + 1)) + 1;  //Math.floor(Math.random() * (max-min+1)+min)
      } while (this.usedNumbers.has(randomNum));
    
      this.usedNumbers.add(randomNum);
      return randomNum;
    }

    createTask()
    {
       let formData: Task = this.taskForm.value as Task;
       formData.taskId = this.generateUniqueId();
       console.log(formData);
       this.kanbanService.saveTask(this.data.boardId , formData).subscribe((data:any)=>{
        let message="Task Created Successfully"; 
      this.kanbanService.showSuccess(message)
        this.dialogRef.close(data.body);
       });
    }

    closeModal(){
      this.dialogRef.close();
     }
}
