import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs'; // handler asynchronous data streams
import { response } from 'express';

@Injectable({
    providedIn: 'root'
}) // can be injected as dependency into other classes
export class Auth {
    // gọi HTTPClient
    private http = inject(HttpClient);

    private apiUrl = 'http://localhost:8080/api/auth/login';

    //-- hàm nhận username, password, và gửi đi
    login(credentials: any): Observable<any> {
        return this.http.post(this.apiUrl, credentials).pipe(
            tap({
                next: (response: any) => {
                    this.saveToken(response.token_ngan_han, response.refreshToken);
                },
                error: (err) => {
                    console.error("Lỗi xảy ra trong quá trình xử lý tap: ", err);
                }
            })
        );
    }

    refreshToken(): Observable<any> {
        const refreshToken = this.getRefreshToken();
        return this.http.post(this.apiUrl, { refreshToken }).pipe(
            tap({
                next: (response: any) => {
                    localStorage.setItem("token_ngan_han", response.token_ngan_han);
                }
            })
        );
    }

    logout() {
        const refreshToken = this.getRefreshToken();
        this.http.post(this.apiUrl, { refreshToken }).subscribe({
            next: () => this.removeToken(),
            error: () => this.removeToken()
        });
    }

    // các hàm xử lý token
    saveToken(token: string, refreshToken: string): void {
        localStorage.setItem('token_ngan_han', token);
        localStorage.setItem('refresh_token', refreshToken);
    }

    getAccessToken(): string | null {
        return localStorage.getItem('token_ngan_han');
    }

    getRefreshToken(): string | null {
        return localStorage.getItem('refreshToken');
    }

    removeToken(): void {
        localStorage.removeItem('token_ngan_han');
        localStorage.removeItem('refreshToken');
    }
    //--
}
