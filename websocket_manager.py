from typing import Dict
from fastapi import WebSocket
from models import Message, UserLocation


class ConnectionManager:
    def __init__(self):
        self.active_connections: Dict[str, WebSocket] = {}

    async def connect(self, user_id: str, websocket: WebSocket):
        self.active_connections[user_id] = websocket

    def disconnect(self, user_id: str):
        if user_id in self.active_connections:
            del self.active_connections[user_id]

    async def send_to_user(self, user_id: str, message: str):
        if user_id in self.active_connections:
            await self.active_connections[user_id].send_text(message)

    async def broadcast(self, user_id: str, message: str):
        m = Message.parse_raw(message)
        if m.type == "user_location":
            m.data["userId"] = user_id
            for user_id, connection in self.active_connections.items():
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