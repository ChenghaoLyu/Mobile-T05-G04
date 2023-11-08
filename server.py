from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from models import Message, UserLocation, Account
from websocket_manager import ConnectionManager
from auth import get_current_user, oauth2_scheme, create_access_token, get_user, verify_password
import json
# from websocket_routes import websocket_endpoint

app = FastAPI()
manager = ConnectionManager()

accounts = {
    "example@mail.com": ["password1","BroJoKer", "0000", "BroJoKer0000"],
    "example2@mail.com": ["password2", "Dino", "0001", "Dino0001"]
}


@app.websocket("/ws/{socket_id}")
async def websocket_route(socket_id: str, websocket: WebSocket):
    await websocket.accept()
    auth_data = await websocket.receive_text()
    auth_message = Message.parse_raw(auth_data)

    # 检查消息类型和token
    if auth_message.type != "auth" or not await manager.authenticate(websocket, auth_message.data.get("token")):
        return
    else:
        await websocket.send_text("Connection established")
        await manager.connect(socket_id, websocket)

    try:
        while True:
            raw_data = await websocket.receive_text()
            try:
                
                message = Message.parse_raw(raw_data)
                # if message.type == "user_location":
                #     user_location = UserLocation.parse_obj(message.data)
                #     # ... 处理user_location数据 ...
                await manager.broadcast(socket_id,raw_data)

                if message.type == "validation":
                    print(message)
                    email = message.data.get("email")
                    password = message.data.get("password")
                    if accounts[email][0] == password:
                        account = {
                            "email": email,
                            "username": accounts[email][1],
                            "hashtag": accounts[email][2],
                            "userID": accounts[email][3]
                        }
                        message.data = account
                        message.type = "account"
                        await manager.send_to_user(socket_id, message)
                    else:
                        print("wrong validation")
                        await websocket.send_text("Validation Fail")

                elif message.type == "registration":
                    registration = Account.parse_obj(message.data)
                elif message.type == "room_information":
                    print("receive roomInformation")
                    print(message)

            except ValueError as e:
                # 这里处理解析错误，例如发送错误响应或记录错误
                print(f"Error parsing message: {e}")

    except WebSocketDisconnect:
        manager.disconnect(websocket)