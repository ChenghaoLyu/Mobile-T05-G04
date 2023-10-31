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

class Validation(BaseModel):
    validation: bool
    token: Token

class Registration(BaseModel):
    validation: bool
    token: Token
    username: str
    hashtag: int
    userID: str

class User(BaseModel):
    username: str

class UserInDB(User):
    hashed_password: str
