from functools import lru_cache

from langchain_openai import ChatOpenAI # llm을 쉽게 돌릴 수 있게 하는 라이브러리(우리는 openapi를 사용한다.)
from pydantic_settings import BaseSettings, SettingsConfigDict # 설정 관련 객체들이 있는 라이브러리

class Settings(BaseSettings):
    """환경변수 기반 설정.

    .env 파일이 있으면 자동 로드합니다.
    """

    openai_api_key: str
    model_name: str = "gpt-5-nano" # 이번에 쓸 모델 (연습때 낮은걸로 하기) / .env에 넣어도 되는데 안될때, 안전장치다.
    port: int = 8000
    request_timeout: float = 30.0

    model_config = SettingsConfigDict( # 설정하는것들에 대한 옵션 파일을 넣는다. 
        env_file=".env", # env 파일을 어디서 어떤걸로 읽을꺼냐?라는 의미
        env_file_encoding="utf-8",
        case_sensitive=False, #대소문자 구분 (이게 기본값이라 빼도 무방하다)
    )

@lru_cache
def get_settings() -> Settings:# 세팅에 관련된건, 이 명을 쓰면 된다. 
    """Settings 싱글톤. 첫 호출 시 1회만 생성됩니다."""
    return Settings()


@lru_cache
def get_llm() -> ChatOpenAI:
    """ChatOpenAI 싱글톤.

    Depends(get_llm)으로 주입받으면 요청마다 객체가 재사용됩니다.
    """
    settings = get_settings()
    return ChatOpenAI( # 이 객체 만들때 필요한 설정을 집어넣어 llm 모듈을 만들었다고 보면 된다. 
        model=settings.model_name,
        api_key=settings.openai_api_key,
        timeout=settings.request_timeout,
    )
