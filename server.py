# # server.py
# from flask import Flask, jsonify
#
# app = Flask(__name__)
#
# @app.route('/')
# def hello_world():
#     return jsonify(message="Hello, Android Client!")
#
# if __name__ == '__main__':
#     app.run(debug=True, host='0.0.0.0', port=5001)
# main.py
from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from models import Message, UserLocation
from websocket_manager import ConnectionManager
from auth import get_current_user, oauth2_scheme, create_access_token, get_user, verify_password
# from websocket_routes import websocket_endpoint

app = FastAPI()
manager = ConnectionManager()

@app.websocket("/ws/{user_id}")
async def websocket_route(websocket: WebSocket, user_id: str):
    auth_data = await websocket.receive_text()
    auth_message = Message.parse_raw(auth_data)

    # 检查消息类型和token
    if auth_message.type != "auth" or not await manager.authenticate(websocket, auth_message.data.get("token")):
        return
    else:
        await websocket.send_text("Connection established")
        await manager.connect(websocket, user_id)

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