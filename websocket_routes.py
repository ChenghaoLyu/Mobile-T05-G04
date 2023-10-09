from fastapi import WebSocket
from .models import Message, UserLocation
from .websocket_manager import manager

@app.websocket("/ws/{user_id}")
async def websocket_endpoint(websocket: WebSocket, user_id: str):
    await manager.connect(websocket, user_id)
    try:
        while True:
            raw_data = await websocket.receive_text()
            try:
                message = Message.parse_raw(raw_data)
                if message.type == "user_location":
                    user_location = UserLocation.parse_obj(message.data)
                    # ... 处理user_location数据 ...
                    await manager.broadcast(raw_data)
                # 可以在这里添加其他消息类型的处理
                # elif message.type == "another_type":
                #     ...

            except ValueError as e:
                # 这里处理解析错误，例如发送错误响应或记录错误
                print(f"Error parsing message: {e}")

    except WebSocketDisconnect:
        manager.disconnect(websocket)
