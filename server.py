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
from .models import Message, UserLocation
from .websocket_manager import ConnectionManager
from .auth import get_current_user, oauth2_scheme, create_access_token, get_user, verify_password
from .websocket_routes import websocket_endpoint

app = FastAPI()
manager = ConnectionManager()

@app.websocket("/ws/{user_id}")
async def websocket_route(websocket: WebSocket, user_id: str):
    await websocket_endpoint(websocket, user_id)