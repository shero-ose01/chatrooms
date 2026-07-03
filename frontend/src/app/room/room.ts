import { Component, OnInit, OnDestroy, signal, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { RoomService } from '../room.service';
import { ChatMessage } from '../room';

@Component({
  selector: 'app-room',
  imports: [FormsModule],
  templateUrl: './room.html',
  styleUrl: './room.scss',
})
export class Room implements OnInit, OnDestroy {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private roomService = inject(RoomService);

  roomName = '';
  sender = '';
  draft = '';
  messages = signal<ChatMessage[]>([]);
  private client?: Client;

  ngOnInit() {
    const name = this.route.snapshot.paramMap.get('name')!;
    this.roomService.getRoom(name).subscribe({
      next: (room) => {
        this.roomName = room.name;
        this.sender = prompt('Your name') || 'Anonymous';
        this.connect();
      },
      error: () => this.router.navigate(['/']),
    });
  }

  private connect() {
    this.client = new Client({
      webSocketFactory: () => new SockJS('/ws'),
      onConnect: () => {
        this.client!.subscribe(`/rooms/room/${this.roomName}`, (frame) => {
          const msg: ChatMessage = JSON.parse(frame.body);
          this.messages.update((list) => [...list, msg]);
        });
        this.client!.publish({
          destination: `/send/room/${this.roomName}/join`,
          body: JSON.stringify({ sender: this.sender }),
        });
      },
    });
    this.client.activate();
  }

  send() {
    if (!this.draft.trim()) return;
    this.client?.publish({
      destination: `/send/room/${this.roomName}/send`,
      body: JSON.stringify({ sender: this.sender, content: this.draft }),
    });
    this.draft = '';
  }

  get shareLink() { return window.location.href; }

  ngOnDestroy() {
    this.client?.deactivate();
  }
  get displayRoomName():string { return this.roomName.replaceAll('_', ' ');}
}
