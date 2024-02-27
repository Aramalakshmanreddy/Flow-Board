import { Component } from '@angular/core';
import { Task } from '../models/task';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Inject } from '@angular/core';
import { KanbanService } from '../services/kanban.service';
@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrls: ['./edit-task.component.css']
})
export class EditTaskComponent {
  originalTask: Task = {};

  constructor(private kanbanService: KanbanService,
    @Inject(MAT_DIALOG_DATA) public data: { boardId: number, stageId: number, task: any, members: [], creator: String },
    private dialogRef: MatDialogRef<EditTaskComponent>) { }

  task: Task = {};
  id!: number;
  minDate: Date = new Date();
  members: [] = this.data.members;
  creator: String = this.data.creator;
  ngOnInit(): void {
    this.originalTask = { ...this.data.task };  // Create a copy of the original Task data

    this.task = { ...this.data.task };  // Use the copy for editing
  }

  editTask() {
    const task = {
      'taskId': this.data.task.taskId,
      'taskName': this.task.taskName,
      'description': this.task.description,
      'assignee': this.task.assignee,
      'dueDate': this.task.dueDate,
      'priority': this.task.priority,
    }


    this.kanbanService.updateTask(this.data.boardId, this.data.stageId, task).subscribe((data: any) => {
      this.dialogRef.close(data.body);
    });
  }

  closeModal() {
    this.task = { ...this.originalTask };
    this.dialogRef.close();
  }
}