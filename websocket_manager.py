from typing import Dict
from fastapi import WebSocket
from models import Message, UserLocation, PositionList
from pydantic import ValidationError


class ConnectionManager:
    def __init__(self):
        self.active_connections: Dict[str, WebSocket] = {}

    async def connect(self, socket_id: str, websocket: WebSocket):
        self.active_connections[socket_id] = websocket

    def disconnect(self, socket_id: str):
        if socket_id in self.active_connections:
            del self.active_connections[socket_id]

    async def send_to_user(self, socket_id: str, message: Message):
        print("----------")
        print(type(message))
    
        # m = Message.parse_raw(message)
        if socket_id in self.active_connections:
            if message.type == "account":
                await self.active_connections[socket_id].send_text(message.json())
            elif message.type == "survival":
                await self.active_connections[socket_id].send_text(message.json())
            elif message.type == "successfully create room":
                await self.active_connections[socket_id].send_text(message.json())
            elif message.type == "get current room":
                await self.active_connections[socket_id].send_text(message.json())
            elif message.type == "get all updated rooms":
                print("response")
                print(message.json())
                await self.active_connections[socket_id].send_text(message.json())
            elif message.type == "updated positions":
                await self.active_connections[socket_id].send_text(message.json())
            elif message.type == "successfully update room":
                    print("sent updated room")
                    for socket_id, connection in self.active_connections.items():
                    # for connection in self.active_connections.values():
                        await connection.send_text(message.json())
            elif message.type == "game start":
                for socket_id, connection in self.active_connections.items():
                # for connection in self.active_connections.values():
                    await connection.send_text(message.json())
    async def broadcast(self, socket_id: str, message: str):
        
        try:
            m = Message.parse_raw(message)
            print(m.type)
            if m.type == "user_location":
                for socket_id, connection in self.active_connections.items():
                # for connection in self.active_connections.values():
                    await connection.send_text(m.json())
            if m.type == "updated positions":
                    for socket_id, connection in self.active_connections.items():
                    # for connection in self.active_connections.values():
                        # print("------")
                        # print(m.json())
                        await connection.send_text(m.json())
            if m.type == "updated pressure":
                    # print("sent pressure")
                    for socket_id, connection in self.active_connections.items():
                    # for connection in self.active_connections.values():
                        await connection.send_text(m.json())
            if m.type == "successfully update room":
                    print("sent updated room")
                    for socket_id, connection in self.active_connections.items():
                    # for connection in self.active_connections.values():
                        await connection.send_text(m.json())
        except ValidationError as e:
            pass
        # try:
        #     # print("trytryr")
        #     m = PositionList.parse_raw(message)
        #     # print(m)
        #     # print("end")
        #     if m.type == "updated positions":
        #             for socket_id, connection in self.active_connections.items():
        #             # for connection in self.active_connections.values():
        #                 print("lalalalalal")
        #                 await connection.send_text(m.json())
        # except ValidationError as e:
        #     pass

    async def authenticate(self, websocket: WebSocket, token: str) -> bool:
        # 这里只是一个简单的硬编码验证。在实际应用中，你应该使用更复杂的方法。
        if token == "YOUR_SECRET_TOKEN":
            return True
        else:
            await websocket.send_text("Authentication failed.")
            await websocket.close(code=4000)
            return False

manager = ConnectionManager()