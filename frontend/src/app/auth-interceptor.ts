import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { Auth } from './auth';

@Injectable
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: Auth) {}

  // using DI-based interceptor (defined as injectable classes, implementing HttpInterceptor interface)
  intercept(request: HttpRequest<any>, next: HttpHandler) : Observable<HttpEvent<any>> {
    // chiều đi: thêm token vào mọi req
    let authReq = request;
    const accessToken = this.authService.getAccessToken();
    const isAuthApi = request.url.includes('/api/auth/');

    // `!AuthApi` vì ko được đưa token vào API đky/đnhap
    if (accessToken && !isAuthApi) {
      authReq = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + accessToken)
      });
    }

    // chiều về: bắt lỗi 401 và xin cấp lại Token
    // lỗi 401 là do hết hạn token (vì thế cần refresh token)
    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status == 401 && !isAuthApi) {
          return this.authService.refreshToken().pipe(
            switchMap((response: any) => {
              // tạo clone cho request (vì request immutable) để thêm token mới
              const newReq = request.clone({
                headers: request.headers.set('Authorization', 'Bearer ' + response.token_ngan_han)
              });

              //gọi api cũ để gửi đi lần 2
              return next.handle(newReq);
            }),
            //nếu refreshToken cũng hết hạn, kick user ra
            catchError((refreshError) => {
              this.authService.removeToken();
              window.location.href = '/login'; // điều hướng về trang login
              return throwError(() => refreshError);
            })
          )
        }
        //bắt các lỗi khác (403, 500), kick user ra như bthg
        return throwError(() => error);
      }) 
    );
  }
}