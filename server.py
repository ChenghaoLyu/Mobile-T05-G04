import random
from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from models import Message, UserLocation, Account, PositionList
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
room_test = {'modeName': 'Classic',
             'requiredRat': 1, 
             'locationName': 'Unimelb', 
             'currentRat': 0,
             'isOngoing': False,
             'hostId': 'Iris0002',
             'isPrivate': True,
             'roomId': '777777', 
             'duration': '30',
             'password': '000000',
             'requiredCat': 1, 
             'ratPlayers': {},
             'catPlayers': {"Iris0002":{"userId":"Dino0001", "userName":"Iris", "isHost": True, "isReady": False}},  
             'currentCat': 1,    
             'startTime': '23:04',  
             'readyList': {},
             'survival': 1
}

room_list = []
room_list.append(room_test)

rooms_dict = {}
rooms_dict[room_test["roomId"]] = room_test

position_test = {
    "BroJoker0000": [-37.7982, 144.9594],
    "Dino0001" : [-37.7962, 144.9594]
}
pressure_test = {
    "BroJoKer0000": 1004.1,
    "Dino0001": 1004.1
}
# position_test2 = {
#     "user1" : [-37.7962, 144.9594]
# }
# player_position_list = {}
# player_position_list.(position_test1)
# player_position_list.append(position_test2)


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
                if message.type == "user_location":
                    await manager.broadcast(socket_id,raw_data)

                elif message.type == "validation":
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
                        print(message)
                        await manager.send_to_user(socket_id, message)
                    else:
                        print("Wrong validation")
                        await websocket.send_text("Validation Fail")

                elif message.type == "registration":
                    accountsData = [element for row in accounts.values() for element in row]
                    email = message.data.get("email")
                    username = message.data.get("username")
                    password = message.data.get("password")
                    hash = generateHash(accountsData, username)
                    if not email in accounts.keys():
                        accInfo = [password, username, hash, username+str(hash)]
                        accounts[email] = accInfo

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
                        print("Registration fails, repetitive email")
                        await websocket.send_text("Validation Fail")

                elif message.type == "survival":
                    # roomId = message.data.get("roomId")
                    # loss = int(message.data.get("loss"))
                    # i = 0
                    # for room in room_list:
                    #     if room['roomId'] == roomId:
                    #         survival = {"survival": room['survival'] - loss}
                    #         room_list[i]["survival"] = room['survival'] - loss
                    #         message.data = survival
                    #         message.type = "survival"
                    #         await manager.send_to_user(socket_id, message)
                    #         break
                    #     i = i + 1
                    await manager.send_to_user(socket_id, message)

                elif message.type == "room_information":
                    print("receive roomInformation")
                    print(message)
                    rooms_dict[message.data.get("roomId")] = message.data
                    for i in rooms_dict.keys():
                        print(i)
                        print(rooms_dict[i])
                    message.type = "successfully create room"
                    await manager.send_to_user(socket_id, message)
                elif message.type == "update_room_information":
                    print("receive updatedformation")
                    print(message)
                    rooms_dict[message.data.get("roomId")] = message.data
                    # for i in rooms_dict.keys():
                    #     print(i)
                    #     print(rooms_dict[i])
                    message.type = "successfully update room"
                    await manager.send_to_user(socket_id, message)
                elif message.type == "request_current_room_information":
                    request_room_id = message.data.get("roomId")
                    message.data = rooms_dict[request_room_id]
                    message.type = "get current room"
                    await manager.send_to_user(socket_id,message.json)

                elif message.type == "request_all_rooms_information":
                    # rooms_json = json.dumps(rooms_dict)
                    message.data = rooms_dict
                    # message.data = rooms_json
                    message.type = "get all updated rooms"
                    print(message)
                    print("received request")
                    await manager.send_to_user(socket_id,message)

                elif message.type == "notify_game_start":
                    message.type = "game start"
                    await manager.send_to_user(socket_id,message)

                elif message.type == "current_position":
                    # print("received current position")
                    # print(message)
                    test_userId = message.data.get("userId")
                    position_test[test_userId] = [message.data.get("latitude"), message.data.get("longitude")]
                    # for userid_ in position_test.keys():
                    #     if userid_ != test_userId:
                    #         position_test[userid_] = [message.data.get("latitude") + 0.002, message.data.get("longitude")]
                    positionList = {
                        "userId" : list(position_test.keys()), 
                        "position" : list(position_test.values())
                    }
                    message.data = positionList
                    message.type = "updated positions"
                    # print(positionList)
                    # print(type(positionList))
                
                    await manager.broadcast(socket_id,message.json())
                    # await manager.send_to_user(socket_id, message)
                elif message.type == "current_pressure":
                    # print("received current pressure")
                    # print(message)
                    test_userId = message.data.get("userId")
                    pressure_test[test_userId] = message.data.get("currentPressure")
                    pressure_result = []
                    player_result = []
                    for userid_ in pressure_test.keys():
                        if userid_ != test_userId:
                            player_result.append(str(userid_))
                            if pressure_test[userid_] - pressure_test[test_userId] >= 0.25:
                                pressure_result.append(int(-1))
                            elif pressure_test[test_userId] - pressure_test[userid_] >= 0.25:
                                pressure_result.append(int(1))
                            else:
                                pressure_result.append(int(0))
                    pressureList = {
                        "userId" : player_result, 
                        "pressure" : pressure_result
                    }
                    message.data = pressureList
                    message.type = "updated pressure"
                    # print(message)
                    # print(type(positionList))
                    await manager.broadcast(socket_id,message.json())
                    # await manager.send_to_user(socket_id, message)
            except ValueError as e:
                # 这里处理解析错误，例如发送错误响应或记录错误
                print(f"Error parsing message: {e}")

    except WebSocketDisconnect:
        manager.disconnect(websocket)


def generateHash(accountsData, username):
    hash = random.randint(0, 9999)
    if username+str(hash) in accountsData:
        return generateHash(accountsData, username)
    else:
        return hash