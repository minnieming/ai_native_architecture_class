import time # 파이썬에 내장된 라이브러리

from fastapi import Request # request로 들어오는것.
from starlette.middleware.base import RequestResponseEndpoint # 여기로 내보내는 것


async def add_process_time(request: Request, call_next: RequestResponseEndpoint):
    """요청 처리 시간을 측정하여 응답 헤더에 기록합니다."""
    start = time.perf_counter() # 현재 시간 기준으로 시간을 잰다. 
    response = await call_next(request) # 다음 함수를 호출해서 잰다. 
    elapsed = time.perf_counter() - start #지금 시간을 잰다. 기존 값들을 가지고
    response.headers["X-Process-Time"] = f"{elapsed:.4f}" # header에 계산된값을 넣는다.
    return response