export class User {
    userId: number
    userName: string;
    userEmail: string;
    userPassword: string;
    userPhone: number;

    constructor() {
        this.userId = 0
        this.userName = '';
        this.userEmail = '';
        this.userPassword = '';
        this.userPhone = 0;
    }
}