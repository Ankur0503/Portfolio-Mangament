import { Component } from '@angular/core';
import { AuthService } from './services/auth-service/auth.service';
import { UserCredentials } from './models/user-model/user-credentials';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'capstone-pm';

  constructor(public authService: AuthService, public router: Router) {
    
  }

  ngOnInit() {
    var userObj = localStorage.getItem('currentUser')
    if(userObj !== null) {
      var jwt = JSON.parse(userObj)
      this.authService.getLoggedInUser().subscribe(response => {
        this.authService.userProfile.user = response.data
        this.authService.userProfile.jwt = jwt
        this.authService.isLoggedIn = true
        this.router.navigateByUrl("/")
      }),
      catchError(error => {
        return throwError(error)
      })
    }
    else {
      this.router.navigateByUrl("/signin")
    }
  }

}
