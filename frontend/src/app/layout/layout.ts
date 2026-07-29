import { Component } from '@angular/core';
import { RouterLinkActive, RouterLinkWithHref, RouterOutlet, Router } from "@angular/router";

@Component({
  selector: 'app-layout',
  imports: [RouterLinkActive, RouterLinkWithHref, RouterOutlet],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout {
  constructor(private router: Router) {}

  onLogout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
