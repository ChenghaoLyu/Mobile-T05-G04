from typing import Dict
from fastapi import WebSocket
from models import Message, UserLocation


class ConnectionManager:
    def __init__(self):
        self.active_connections: Dict[str, WebSocket] = {}

    async def connect(self, socket_id: str, websocket: WebSocket):
        self.active_connections[socket_id] = websocket

    def disconnect(self, socket_id: str):
        if socket_id in self.active_connections:
            del self.active_connections[socket_id]

    async def send_to_user(self, socket_id: str, message: Message):
        # m = Message.parse_raw(message)
        if socket_id in self.active_connections:
            if message.type == "account":
                await self.active_connections[socket_id].send_text(message.json())

    async def broadcast(self, socket_id: str, message: str):
        m = Message.parse_raw(message)
        if m.type == "user_location":
            for socket_id, connection in self.active_connections.items():
            # for connection in self.active_connections.values():
                await connection.send_text(m.json())

    async def authenticate(self, websocket: WebSocket, token: str) -> bool:
        # 这里只是一个简单的硬编码验证。在实际应用中，你应该使用更复杂的方法。
        if token == "YOUR_SECRET_TOKEN":
            return True
        else:
            await websocket.send_text("Authentication failed.")
            await websocket.close(code=4000)
            return False

manager = ConnectionManager()