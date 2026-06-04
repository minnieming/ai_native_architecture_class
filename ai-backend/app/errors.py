"""전역 예외 핸들러.

HTTPException은 FastAPI 기본 핸들러가 처리하므로 등록하지 않습니다.
예측하지 못한 예외(LangChain/CrewAI 내부 오류 등)만 표준 500으로 변환합니다.
"""

import logging # 기본적으로 파이썬에서 제공해준다. 

from fastapi import Request
from fastapi.responses import JSONResponse

logger = logging.getLogger(__name__) # 선언. 로깅을 써서 기본적인 로거를 만든 것. 


async def handle_unexpected(request: Request, exc: Exception) -> JSONResponse: # (범용) 모든 예외처리를 한다. (이건 배우는거라 그렇고, 실제로는 상황에 맞춰서 핸들링하는게 포인트!!) / 응답식은 json 객체로 보내진다
    """모든 미처리 예외를 표준 500 응답으로 변환합니다."""
    logger.exception("unexpected error on %s %s", request.method, request.url.path) # exception오류를 찍어주는 것 ex) post, chat에서 에러가 남
    return JSONResponse(
        status_code=500, # 무조건 상태코드가 있어야 한다. 범용이기 때문에 서버에러라서 500 세팅
        content={ # 이게 중요하다. response의 body 부분이다. 에러는 규격을 만들어서 그대로 움직이게 한다. 
            "error": "internal",
            "detail": str(exc), # 이유
            "path": request.url.path, # 위치
        },
    )
