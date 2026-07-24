import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Auth } from '../auth';
import { response } from 'express';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {
  username = '';
  password = '';

  private auth = inject(Auth);

  onSubmit() {
    const payload = {
      username: this.username,
      password: this.password
    };

    this.auth.login(payload).subscribe({
      next: (response) => {
        const token = response.token;
        if (token) {
          // lưu token
          this.auth.saveToken(token);
          alert("Đăng nhập thành công! Đã lưu token.")
        }
        
      },
      error: (err) => {
        console.error("Lỗi: ", err);
        alert(err.error.message);
      }
    });
  }
}


