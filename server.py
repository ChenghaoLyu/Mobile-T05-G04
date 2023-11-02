
from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from models import Message, UserLocation, Validation, Registration
from websocket_manager import ConnectionManager
from auth import get_current_user, oauth2_scheme, create_access_token, get_user, verify_password
# from websocket_routes import websocket_endpoint

app = FastAPI()
manager = ConnectionManager()

@app.websocket("/ws/{user_id}")
async def websocket_route(user_id: str, websocket: WebSocket):
    await websocket.accept()
    auth_data = await websocket.receive_text()
    auth_message = Message.parse_raw(auth_data)

    # 检查消息类型和token
    if auth_message.type != "auth" or not await manager.authenticate(websocket, auth_message.data.get("token")):
        return
    else:
        await websocket.send_text("Connection established")
        await manager.connect(user_id, websocket)

    try:
        while True:
            raw_data = await websocket.receive_text()
            try:
                message = Message.parse_raw(raw_data)
                # if message.type == "user_location":
                #     user_location = UserLocation.parse_obj(message.data)
                #     # ... 处理user_location数据 ...
                await manager.broadcast(user_id,raw_data)
                # 可以在这里添加其他消息类型的处理
                # elif message.type == "another_type":
                #     ...
                if message.type == "validation":
                    validation = Validation.parse_obj(message.data)
                elif message.type == "registration":
                    registration = Registration.parse_obj(message.data)
                elif message.type == "room_information":
                    print("receive roomInformation")
                    print(message)

            except ValueError as e:
                # 这里处理解析错误，例如发送错误响应或记录错误
                print(f"Error parsing message: {e}")

    except WebSocketDisconnect:
        manager.disconnect(websocket)