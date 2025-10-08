import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Tarefa } from '../models/tarefa.model';

@Injectable({
  providedIn: 'root'
})
export class TarefaService {

  private apiUrl = `${environment.apiUrl}/tarefas`;

  constructor(private http: HttpClient) {}

  listar(page: number = 0, size: number = 10, idProjeto?: number, sortBy: string = 'dataCriacao', sortDir: 'asc' | 'desc' = 'desc'): Observable<any> {
    let params: any = { page, size, sortBy, sortDir };
    if (idProjeto) params.idProjeto = idProjeto;
    return this.http.get(`${this.apiUrl}`, { params });
  }

  criar(tarefa: Tarefa): Observable<Tarefa> {
    return this.http.post<Tarefa>(this.apiUrl, tarefa);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
