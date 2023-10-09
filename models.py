from pydantic import BaseModel

class Message(BaseModel):
    type: str
    data: dict

class UserLocation(BaseModel):
    user_id: str
    latitude: float
    longitude: float
    timestamp: int

class Token(BaseModel):
    access_token: str
    token_type: str

class User(BaseModel):
    username: str

class UserInDB(User):
    hashed_password: str
