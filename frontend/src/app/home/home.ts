import {Component, inject, signal, OnInit} from '@angular/core';
import {RoomService} from '../room.service';
import {RoomSummary} from '../room';
import {Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [FormsModule, DatePipe],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home implements OnInit {
  rooms = signal<RoomSummary[]>([]);
  newRoomName = '';
  error = signal<string | null>(null);

  private roomService = inject(RoomService);
  private router = inject(Router);

  ngOnInit(){
    this.load();
  }

  load(){
    this.roomService.listRooms().subscribe(r => this.rooms.set(r));
  }

  create(){
    this.roomService.createRoom(this.newRoomName).subscribe(
      {
        next: r => {
          this.newRoomName = '';
          this.load();
          this.router.navigate(['/room',r.name]);
        },
        error: e => {
            if(e.status === 409){
              this.error.set("That room name is already taken");
              }else if(e.status === 400){
                this.error.set(e.error?.detail?? "Invalid room name");
                }else{
                  this.error.set("Something went wrong. Please try again");
                  }
          }
      }
    )
  }

  join(name: string){
    this.router.navigate(['/room',name]);
  }

 displayRoomName(name:string):string{ return name.replaceAll('_', ' ');}

}
