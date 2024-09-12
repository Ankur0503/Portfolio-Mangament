import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export function validatePhone(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;

        if (!value) {
            return null; // If no value, don't apply numeric validation. Let required handle it if needed.
        }

        const isNumeric = /^[0-9]*$/.test(value);

        return isNumeric ? null : { notNumeric: true };
    };
}