import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { TarefaService } from '../../../core/services/tarefa.service';
import { Tarefa } from '../../../core/models/tarefa.model';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tarefa-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  templateUrl: './tarefa-list.component.html',
  styleUrls: ['./tarefa-list.component.scss']
})
export class TarefaListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'titulo', 'status', 'acoes'];
  dataSource = new MatTableDataSource<Tarefa>([]);
  totalElements = 0;
  pageSize = 10;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private tarefaService: TarefaService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarTarefas();
  }

  carregarTarefas(page: number = 0): void {
    this.tarefaService.listar(page, this.pageSize).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
      },
      error: () => this.snackBar.open('Erro ao carregar tarefas', 'Fechar', { duration: 3000 })
    });
  }

  excluirTarefa(id: number): void {
    if (confirm('Deseja realmente excluir esta tarefa?')) {
      this.tarefaService.excluir(id).subscribe({
        next: () => {
          this.snackBar.open('Tarefa excluída com sucesso!', 'Fechar', { duration: 3000 });
          this.carregarTarefas();
        },
        error: () => this.snackBar.open('Erro ao excluir tarefa', 'Fechar', { duration: 3000 })
      });
    }
  }

  novaTarefa(): void {
    this.router.navigate(['/tarefas/nova']);
  }

  onPageChange(event: any): void {
    this.carregarTarefas(event.pageIndex);
  }
}
