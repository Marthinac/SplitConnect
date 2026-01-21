import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-list-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list-user.html',
  styleUrl: './list-user.css',
})
export class ListUser implements OnInit {

  users: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.buscarUsuarios();
  }

  buscarUsuarios() {
    console.log("Iniciando busca")
    this.http.get<any[]>('http://localhost:8080/users/mock')
      .subscribe({
        next: (data) => {
          console.log("users recebidos", data)
          this.users = data;
        },
        error: (err) => {
          console.error('Erro na requisição', err);
        }
      });
  }
}
