import { Routes } from '@angular/router';
import { TarefaListComponent } from './features/tarefas/tarefa-list/tarefa-list.component';
import { TarefaFormComponent } from './features/tarefas/tarefa-form/tarefa-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'tarefas', pathMatch: 'full' },
  { path: 'tarefas', component: TarefaListComponent },
  { path: 'tarefas/nova', component: TarefaFormComponent }
];
