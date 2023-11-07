from pydantic import BaseModel

class Message(BaseModel):
    type: str
    data: dict

class UserLocation(BaseModel):
    userId: str
    latitude: float
    longitude: float
    timestamp: int

class Token(BaseModel):
    access_token: str
    token_type: str

class Account(BaseModel):
    email: str
    username: str
    hashtag: int
    userID: str

class User(BaseModel):
    username: str

class UserInDB(User):
    hashed_password: str
