from pydantic import BaseModel, Field

class ChatRequest(BaseModel): # basemodel : pythantic에 있는 부모를 상속 받음 -> 검증 객체가 됨.
    """LangChain /chat 요청 본문.

    사용자 식별은 Spring 게이트웨이가 JWT로 처리하므로
    Python은 prompt만 받습니다. (Spring이 보내는 user_id 등 추가 필드는
    Pydantic이 기본 무시하므로 호환에 문제 없습니다.)
    """ # 스웨거에 넣을 설명

    prompt: str = Field(..., min_length=1, max_length=2000, description="사용자 질문") # prompt는 속성 중 하나 
    # description : 속성자체의 힌트 (설명이라고 보면 된다.) 여기에 무슨 내용이 들어갈지에 대한. 

    model_config = { # 스웨거에서 예시로 나오는 부
        "json_schema_extra": {
            "example": {"prompt": "안녕하세요"}
        }
    }

class ChatResponse(BaseModel): # 보통 서버에서 내려옴.
    """채팅 응답 본문."""

    answer: str = Field(..., description="모델 응답 본문")
    model: str = Field(..., description="실제 사용된 모델 식별")