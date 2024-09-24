import { UserProfile } from "./user-profile";

export class UserResponse {
    user: UserProfile
    jwt: string;
    constructor() {
        this.user = new UserProfile()
        this.jwt = ''
    }
}