export class UserProfile {
    userId: number
    userName: string
    userEmail: string
    userPassword: string
    userPhone: number
    userAge: number;
    userRole: string;

    constructor() {
        this.userId = 0,
        this.userName = ''
        this.userEmail = ''
        this.userPassword = ''
        this.userPhone = 0
        this.userAge = 0
        this.userRole = ''
    }
}