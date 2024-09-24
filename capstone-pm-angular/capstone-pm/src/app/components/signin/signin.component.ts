import { Component } from '@angular/core';
import { User } from '../../models/user-model/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateEmail } from '../../validators/email-validator';
import { validatePassword } from '../../validators/password-validator';
import { UserCredentials } from 'src/app/models/user-model/user-credentials';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent {
  email: FormControl
  password: FormControl
  loginForm: FormGroup

  constructor(public authService: AuthService, public router: Router) {
    this.email = new FormControl('', [Validators.required, validateEmail()])
    this.password = new FormControl('', [Validators.required, Validators.minLength(8), validatePassword()])

    this.loginForm = new FormGroup({
      email: this.email,
      password: this.password
    })
  }

  signInOnSubmit() {
    let userCredentials: UserCredentials = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }

    this.authService.loginUser(userCredentials).subscribe(
      response => {
        this.router.navigateByUrl("/funds/filter")
      },
      err => {
        console.error('Login Failed', err)
      }
    )
  }
}
