import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs'; // handler asynchronous data streams
import { isPlatformBrowser } from '@angular/common';

@Injectable({
    providedIn: 'root'
}) // can be injected as dependency into other classes
export class Auth {
    // Tiêm PLATFORM_ID vào constructor để Angular biết môi trường hiện tại
    constructor(
        @Inject(PLATFORM_ID) private platformId: Object,
        private http: HttpClient
    ) {}

    private apiUrl = 'http://localhost:8080/api/auth';

    //-- hàm nhận username, password, và gửi đi
    login(credentials: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
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
        return this.http.post(`${this.apiUrl}/refresh`, { refreshToken }).pipe(
            tap({
                next: (response: any) => {
                    if (isPlatformBrowser(this.platformId)){
                        localStorage.setItem("token_ngan_han", response.token_ngan_han);
                    }
                }
            })
        );
    }

    logout() {
        const refreshToken = this.getRefreshToken();
        this.http.post(`${this.apiUrl}/logout`, { refreshToken }).subscribe({
            next: () => this.removeToken(),
            error: () => this.removeToken()
        });
    }

    // các hàm xử lý token
    saveToken(token: string, refreshToken: string): void {
        localStorage.setItem('token_ngan_han', token);
        localStorage.setItem('refreshToken', refreshToken);
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
