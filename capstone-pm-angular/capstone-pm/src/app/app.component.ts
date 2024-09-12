import { Component } from '@angular/core';
import { AuthService } from './services/auth-service/auth.service';
import { UserCredentials } from './models/user-credentials';
import { Router } from '@angular/router';

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

    if (userObj !== null) {
      var myUserDetails = JSON.parse(userObj)
      var userCreds: UserCredentials = new UserCredentials()
      userCreds.email = myUserDetails.user.userEmail
      userCreds.password = myUserDetails.password
      console.log(userCreds)
      this.authService.loginUser(userCreds)
    }
    else
      this.router.navigateByUrl("/signin")
  }

}
