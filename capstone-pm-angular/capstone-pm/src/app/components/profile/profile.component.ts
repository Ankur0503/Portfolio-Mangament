import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from 'src/app/services/auth-service/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  userProfile = {
    userId: '',
    userName: '',
    userPassword: '',
    userPhone: ''
  };

  constructor(public authService: AuthService) {}

  ngOnInit() {
    this.authService.getLoggedInUser().subscribe(response => {
      this.userProfile.userId = response.data.userId
      this.userProfile.userName = response.data.userName
      this.userProfile.userPhone = response.data.userPhone
    })
  }
  
  isEditing = false;

  onEdit() {
    this.isEditing = true;
  }

  onSave(form: NgForm) {
    if (form.valid) {
      this.userProfile.userName = form.value.userName;
      this.userProfile.userPassword = form.value.userPassword;
      this.userProfile.userPhone = form.value.userPhone;
      
      this.authService.updateUser(this.userProfile).subscribe(response => {

      })
      this.isEditing = false;
    }
  }

  onCancel(form: NgForm) {
    this.isEditing = false;
    form.resetForm();
  }
}
