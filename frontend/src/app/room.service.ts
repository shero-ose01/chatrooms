import { Service, Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RoomSummary } from './room';

@Service()
export class RoomService {
  private http = inject(HttpClient);

  listRooms(): Observable<RoomSummary[]>{
    return this.http.get<RoomSummary[]>('/rooms');
  }

  createRoom(name: string):Observable<RoomSummary>{
    return this.http.post<RoomSummary>('/rooms', {name});
  }

  getRoom(name: string) : Observable<RoomSummary>{
    return this.http.get<RoomSummary>('/rooms/'+encodeURIComponent(name));
  }
}
