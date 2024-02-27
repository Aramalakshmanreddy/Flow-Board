import { Task } from "./task";

export type Stage = {
    stageId?: number;
    stageName?: string;
    tasks?: Task[];
}