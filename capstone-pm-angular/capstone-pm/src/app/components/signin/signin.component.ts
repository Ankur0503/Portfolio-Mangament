import { Component } from '@angular/core';
import { User } from '../../models/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateEmail } from '../../validators/email-validator';
import { validatePassword } from '../../validators/password-validator';
import { UserCredentials } from 'src/app/models/user-credentials';
import { AuthService } from 'src/app/services/auth-service/auth.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent {
  email: FormControl
  password: FormControl
  loginForm: FormGroup

  constructor(public authService: AuthService) {
    this.email = new FormControl('', [Validators.required, validateEmail()])
    this.password = new FormControl('', [Validators.required, Validators.minLength(8), validatePassword()])

    this.loginForm = new FormGroup({
      email: this.email,
      password: this.password
    })
  }

  signInOnSubmit() {
    console.log(this.loginForm.value)
    let userCredentials: UserCredentials = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }
    this.authService.loginUser(userCredentials)
  }
}
