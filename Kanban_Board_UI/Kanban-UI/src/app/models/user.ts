import { Board } from "./board";

export type User = {
    email?: string;
    userName?: string;
    boards?: Board[];
}