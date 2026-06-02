from fastapi import FastAPI

app = FastAPI(title="AI Backend", version="1.0.0")

@app.get("/health", tags=["meta"]) # tags는 문서에서 쓰는것, 태그끼리 묶어준다.
def health() -> dict[str,str]: # json 객체 만들때 쓰는 문법, key, value의 타입을 지정하는 것. -> 함수는 json형태로 내려갈꺼다
    """헬스 체크 - Docker/k8s liveness 용도."""
    return {"status":"ok"} # 위에서 DICT로 지정한게 이 형식이다. -> 이후에 있는건 RETURN 값에 대한 타입 힌트 (아니어도 된다고 함...!)