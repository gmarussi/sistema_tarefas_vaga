import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ProjetoService } from '../../../core/services/projeto.service';
import { TarefaService } from '../../../core/services/tarefa.service';
import { Projeto } from '../../../core/models/projeto.model';

@Component({
  selector: 'app-tarefa-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule,
    MatSnackBarModule
  ],
  templateUrl: './tarefa-form.component.html',
  styleUrls: ['./tarefa-form.component.scss']
})
export class TarefaFormComponent implements OnInit {
  form!: FormGroup;
  projetos: Projeto[] = [];
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private projetoService: ProjetoService,
    private tarefaService: TarefaService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      idProjeto: [null, Validators.required],
      titulo: ['', [Validators.required, Validators.maxLength(100)]],
      descricao: ['', [Validators.maxLength(500)]],
      status: ['']
    });

    this.carregarProjetos();
  }

  carregarProjetos(): void {
    this.projetoService.listar().subscribe({
      next: (projetos) => this.projetos = projetos,
      error: () => this.snackBar.open('Erro ao carregar projetos', 'Fechar', { duration: 3000 })
    });
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.tarefaService.criar(this.form.value).subscribe({
      next: () => {
        this.snackBar.open('Tarefa criada com sucesso!', 'Fechar', { duration: 3000 });
        this.router.navigate(['/tarefas']);
      },
      error: () => {
        this.snackBar.open('Erro ao criar tarefa', 'Fechar', { duration: 3000 });
        this.submitting = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/tarefas']);
  }
}
