"""LangChain 기반 단일 체인 /chat 라우트.

핵심 패턴:
- 동기 invoke는 이벤트 루프를 차단합니다 → 반드시 ainvoke 사용
- ChatOpenAI는 Depends로 주입받아 재사용
"""

from fastapi import APIRouter, Depends 
from langchain_core.prompts import ChatPromptTemplate # langchain 프롬포트 템플릿 만들수 있는것, 그 중에 해당 템플릿을 가져옴 
from langchain_openai import ChatOpenAI

from ..dependencies import Settings, get_llm, get_settings # .. 이건 위치를 나타낸 
from ..schemas import ChatRequest, ChatResponse # 해당 클래스의 객체를 가져온다. 

router = APIRouter(prefix="/chat", tags=["chat"]) # apirouter객체를 가져와서 쓴다. prefix=최상단의 위치 챗으로 고정
# 라우터를 하나로 만들어서 app으로 보내는 것 


@router.post("", response_model=ChatResponse) # 모델 지워도 상관없음 
async def chat( # 함수 자체를 비동기 해야하기 때문에 async를 달아준다. 
    req: ChatRequest,
    llm: ChatOpenAI = Depends(get_llm), # 여기서 의존성 주입이 나온다. llm : 타입 / 값으로 의존성 주입을 함 @lru_cache에서 한거.
    settings: Settings = Depends(get_settings), # 여기에도 의존성 주입
) -> ChatResponse: # 이 모양으로 내보냄
    """LangChain ChatOpenAI 단일 체인 호출.""" # 이 부분은 스웨거에게 주는 힌트 
    prompt = ChatPromptTemplate.from_messages( # 템플릿을 만들어서 보낸다. 그걸 메세지에 넣는것
        [
            ("system", "You are a helpful assistant. Respond in Korean."),
            ("human", "{q}"), # 밑에 지정이 된다. 인보크 할때 
        ]
    )
    chain = prompt | llm # 프롬포트 타서 llm으로 가서 결과를 줄 체인을 만듬 (여기는 parser가 없다)
    result = await chain.ainvoke({"q": req.prompt}) # 그걸 ainvoke로 실행을 한다. 매개변수로 프롬포트를 q에 넣어서 실행한다. 
    return ChatResponse(answer=result.content, model=settings.model_name) # 파서가 없으니까 content가서 직접 갑을 뽑는다. 
