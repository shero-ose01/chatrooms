export interface RoomSummary {
  name: string;
  createdAt: string;
  userCount: number;
}

export interface ChatMessage{
  sender: string;
  content: string;
  sentAt: string;
}
