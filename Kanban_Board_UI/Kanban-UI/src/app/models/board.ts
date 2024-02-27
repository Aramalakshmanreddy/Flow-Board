import { Stage } from "./stage";

export type Board = {
    boardId?: number;
    boardName?: string;
    description?: string;
    creator?: string;
    creationDate?: Date;
    members?: string[];
    stages?: Stage[];
    membersRef?: string;
    creatorEmail?: string;
}