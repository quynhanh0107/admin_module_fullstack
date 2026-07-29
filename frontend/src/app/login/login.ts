import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../auth';

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
  private route = inject(Router);

  onSubmit() {
    const payload = {
      username: this.username,
      password: this.password
    };

    this.auth.login(payload).subscribe({
      next: (response: any) => {
        const accessToken = response.token_ngan_han;
        const refreshToken = response.refreshToken;
        if (accessToken && refreshToken) {
          // lưu token
          this.auth.saveToken(accessToken, refreshToken);
          alert("Đăng nhập thành công!");

          // điều hướng về trang chủ
          this.route.navigate(['/']);
        }
        
      },
      error: (err) => {
        console.error("Lỗi: ", err);
        alert(err.error.message || "Đăng nhập thất bại. Vui lòng kiểm tra lại tên đăng nhập/mật khẩu!");
      }
    });
  }
}


