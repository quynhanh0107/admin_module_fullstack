import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'; // handler asynchronous data streams

@Injectable({
    providedIn: 'root'
}) // can be injected as dependency into other classes
export class Auth {
    // gọi HTTPClient
    private http = inject(HttpClient);

    private apiUrl = 'http://localhost:8080/api/auth/login';

    //-- hàm nhận username, password, và gửi đi
    login(credentials: any): Observable<any> {
        return this.http.post(this.apiUrl, credentials);
    }

    // các hàm xử lý token
    saveToken(token: string): void {
        localStorage.setItem('token_ngan_han', token);
    }

    getToken(): string | null {
        return localStorage.getItem('token_ngan_han');
    }

    removeToken(): void {
        localStorage.removeItem('token_ngan_han');
    }
    //--
}
