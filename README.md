# Chatrooms Spring Webapp

A Chatroom system made with Spring and Angular. I self host it similar to my other projects under chat.s-ose.de with Cloudflared Tunnel (thanks Vodafone...).
As this is a learning project and focused on Spring (and Angular) i handcoded everything and used AI mainly for the scss (did the finishing touches myself).

# Screenshots
<img width="385" height="206" alt="image" src="https://github.com/user-attachments/assets/64ea6d02-8e58-4931-88e1-405a2170707f" />
<img width="531" height="443" alt="image" src="https://github.com/user-attachments/assets/cb7af03e-c72b-474f-83b8-2a98bf483704" />


## How it works / Features:
The homepage lists open chatrooms which can be clicked to join or you can create your own for others to join. You can choose a name when joining.
Each instance lasts as long as there are users in the room or closes when its empty for 5 minutes.
Room messaging and logic is done with Websockets, STOMP, and since its not account based, when reloading the page or reconnecting you are basically a "new" user.

## Tech Stack
**Backend**
- Java
- Spring Boot 4.1 REST API for Room Management
- Spring Websocket + STOMP for Room Chat Messages
- H2 in memory db, since i didn't want to have a persistent one for this
- Maven, build and deployment

**Frontend**
- Angular
- STOMP.js + SockJS
- Scss (mainly written by AI, did finishing touches)

Also setup Docker/Docker compose and Github Actions self hosted Runner for auto build, tests & deploy on master push.
The Dockerfile builds the Angular app, copies it into Springs static directory and serves both from one JAR.

## Run locally
-/mvnw spring-boot:run
ng serve --proxy-config proxy.conf.json