import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { SessionLogService } from '../services/session-log.service';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent {
 
  errorStatus: string | null = null;

  registrationForm = this.fb.group({
    email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+[a-zA-Z0-9._%+-]*@[a-zA-Z]+[a-zA-Z0-9._%+-]*\.[a-zA-Z]{2,4}$/)]],
    userName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z][a-zA-Z0-9 .\-_]{2,}$/)]],
    password: ['', [Validators.required, Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,}$/)]],
    confirmPassword: ['', [Validators.required]]
  }, { validators: [this.confirmPasswordMatchValidator.bind(this)] });

  constructor(private fb: FormBuilder, private authenticationService: AuthenticationService,private sessionLogService: SessionLogService, private router: Router) { }

  get email() { return this.registrationForm.get('email'); }
  get userName() { return this.registrationForm.get('userName'); }
  get password() { return this.registrationForm.get('password'); }
  get confirmPassword() { return this.registrationForm.get('confirmPassword'); }

  //custom method to validate email
  confirmPasswordMatchValidator(control: AbstractControl) {
    const passwordValue = control.get('password')?.value;
    const confirmPasswordValue = control.get('confirmPassword')?.value;

    if (!passwordValue || !confirmPasswordValue || passwordValue === confirmPasswordValue)
      return null;
    else {
      control.get('confirmPassword').setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
  }

  registerUser() {
    const userDetails = {
      email: this.registrationForm.value.email,
      userName: this.registrationForm.value.userName,
      password: this.registrationForm.value.password
    }

    this.authenticationService.saveUser(userDetails).subscribe({
      next: data => {
        console.log("Data sent successfully " + data);

        this.authenticationService.loginUser(userDetails).subscribe({
          next: data => {
            console.log(data);
            if (data) {
              localStorage.setItem('token', data);
              this.sessionLogService.login();
              this.router.navigate(["dashboard"]);
            }
          }
        });
      },
      error: err => {
        console.log(err.error);
        this.errorStatus = err.error.status;
      }
    });


  }
}



