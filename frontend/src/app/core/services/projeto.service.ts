import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Projeto } from '../models/projeto.model';

@Injectable({
  providedIn: 'root'
})
export class ProjetoService {
  private apiUrl = `${environment.apiUrl}/projetos`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Projeto[]> {
    return this.http.get<Projeto[]>(this.apiUrl);
  }
}
