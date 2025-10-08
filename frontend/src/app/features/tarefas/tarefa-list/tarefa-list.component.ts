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
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { Projeto } from '../../../core/models/projeto.model';
import { ProjetoService } from '../../../core/services/projeto.service';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-tarefa-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatSelectModule,
    MatChipsModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './tarefa-list.component.html',
  styleUrls: ['./tarefa-list.component.scss']
})
export class TarefaListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'titulo', 'projeto', 'dataCriacao', 'status', 'acoes'];
  dataSource = new MatTableDataSource<Tarefa>([]);
  totalElements = 0;
  pageSize = 10;
  loading = false;
  projetos: Projeto[] = [];
  selectedProjetoId?: number;
  sortDir: 'asc' | 'desc' = 'desc';

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private tarefaService: TarefaService,
    private projetoService: ProjetoService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarProjetos();
    this.carregarTarefas();
  }

  carregarProjetos(): void {
    this.projetoService.listar().subscribe({
      next: (ps) => this.projetos = ps,
      error: () => this.snackBar.open('Erro ao carregar projetos', 'Fechar', { duration: 3000 })
    });
  }

  carregarTarefas(page: number = 0): void {
    this.loading = true;
    this.tarefaService.listar(page, this.pageSize, this.selectedProjetoId, 'dataCriacao', this.sortDir).subscribe({
      next: (response) => {
        this.dataSource.data = response.content;
        this.totalElements = response.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.snackBar.open('Erro ao carregar tarefas', 'Fechar', { duration: 3000 });
      }
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
    this.pageSize = event.pageSize;
    this.carregarTarefas(event.pageIndex);
  }

  onProjetoChange(): void {
    this.paginator.firstPage();
    this.carregarTarefas(0);
  }

  onSortDirChange(): void {
    this.paginator.firstPage();
    this.carregarTarefas(0);
  }
}
