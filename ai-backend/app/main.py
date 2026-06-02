from fastapi import FastAPI

app = FastAPI(title="AI Backend", version="1.0.0")

# .을 넣으면 위까지 안가고 여기에 있다라는거다. // 해당 폴더에서 모듈을 가져올 수 있도록 import 해주기
from .schemas import ChatRequest, ChatResponse 

@app.get("/health", tags=["meta"]) # tags는 문서에서 쓰는것, 태그끼리 묶어준다.
def health() -> dict[str,str]: # json 객체 만들때 쓰는 문법, key, value의 타입을 지정하는 것. -> 함수는 json형태로 내려갈꺼다
    """헬스 체크 - Docker/k8s liveness 용도."""
    return {"status":"ok"} # 위에서 DICT로 지정한게 이 형식이다. -> 이후에 있는건 RETURN 값에 대한 타입 힌트 (아니어도 된다고 함...!)

@app.post("/echo", response_model=ChatResponse, tags=["meta"]) # response model : 이걸로 내보낼꺼야 라고 하는 것(없는게 최신식이라고 한다 함). 이거 생량하고 다음줄에 있는걸로 내보낸다
def echo(req: ChatRequest) -> ChatResponse: # request를 저런 모양으로 받을꺼다. / response는 저 형식으로 받을꺼야라는 것
    """pydantic v2 검증 시연용 echo 엔드포인트"""
    return ChatResponse(answer=req.prompt, model="echo-1") # response의 형식에 맞게 ()안에 넣어서 보내는 것. 
