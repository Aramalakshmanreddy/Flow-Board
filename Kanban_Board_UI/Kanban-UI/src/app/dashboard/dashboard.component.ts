import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { BoardListComponent } from '../board-list/board-list.component';
import { ViewBoardComponent } from '../view-board/view-board.component';


import { MatDialog } from '@angular/material/dialog';
import { NewBoardComponent } from '../new-board/new-board.component';
import { KanbanService } from '../services/kanban.service';
import { TokenInterceptorService } from '../services/token-interceptor.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  title: String = "Kanban Board";
  @ViewChild(BoardListComponent) boardListComponent: BoardListComponent;
  @ViewChild(ViewBoardComponent) viewBoardComponent: ViewBoardComponent;

  constructor(private kanbanService: KanbanService, private dialog: MatDialog, private tokenInterceptorService:TokenInterceptorService) {

  }
  public boardList = null;
  public boardList1 = null;
  public viewBoard = null;
  public viewBoardFlag: boolean = true
  public creator: string = "";
  public userName: string = "";

  public searchQuery: string = '';
  ngOnInit(): void {
    this.userName = this.tokenInterceptorService.getUserName();
    this.kanbanService.getBoardList().subscribe((response: any) => {
      let data = response.body;
      this.creator = data.userName;
      this.boardList = data.boards;
      if (this.viewBoardFlag)
        this.viewBoard = this.boardList[0];
      console.log(this.viewBoardFlag);
    });
  }

  newBoard() {
    const modalResponse = this.dialog.open(NewBoardComponent, {
      disableClose: true,
    });
    modalResponse.afterClosed().subscribe((result: any) => {
      if (result) {
        this.viewBoard = result
        this.viewBoardFlag = false;
        this.ngOnInit();
      } else {
        console.log('Modal closed without a result');
      }
    });

  }
  boardlist(event) {
    if (event.boardDeleted || event.boardUpdated) {
      if (event.boardUpdated) {
        this.viewBoardFlag = false;
      }
      if (event.boardDeleted) {
        this.viewBoardFlag = true;
      }
      this.ngOnInit();
    } else {
      this.viewBoard = event;
    }
  }

  applySearch() {
    if (this.searchQuery.trim() === '') {
      this.boardListComponent.boardListData = this.boardList;
    } else {
      this.boardListComponent.boardListData = this.boardList.filter(board =>
        board.boardName.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        board.description.toLowerCase().includes(this.searchQuery.toLowerCase())
      );
    }
  }
}
