import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth-service/auth.service';
import { validatePassword } from 'src/app/validators/password-validator';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profileForm!: FormGroup;
  isEditing = false;

  constructor(private fb: FormBuilder, public authService: AuthService) {}

  ngOnInit(): void {
    // Initialize the form with default values
    this.profileForm = this.fb.group({
      userName: ['', [Validators.required]],
      userPassword: ['', [Validators.required, validatePassword()]],
      userPhone: ['', [Validators.required]]
    });

    // Get user profile data from the service
    this.authService.getLoggedInUser().subscribe(response => {
      this.profileForm.patchValue({
        userName: response.data.userName,
        userPhone: response.data.userPhone,
        // You might not want to prefill the password field
      });
    });
  }

  onEdit() {
    this.isEditing = true;
    this.profileForm.enable();
  }

  onSave() {
    if (this.profileForm.valid) {
      const updatedProfile = this.profileForm.value;

      // Save the updated profile using AuthService
      this.authService.updateUser(updatedProfile).subscribe(response => {
        // Handle the response here
      });

      this.isEditing = false;
      this.profileForm.disable(); // Disable form after save
    }
  }

  onCancel() {
    this.isEditing = false;
    this.profileForm.reset(); // Reset the form to initial values
    this.ngOnInit(); // Reinitialize form with original data
  }
}
