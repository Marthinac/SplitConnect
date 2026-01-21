import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ListUser} from './list-user/list-user';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ListUser],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('SplitConnect');
}
