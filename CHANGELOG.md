# Changelog

All notable changes to this project will be documented in this file.

## [1.1.0] - 2026-03-21

### Added
- 다국어 지원: 한국어 · English · 日本語 (앱 상단 언어 버튼으로 전환)
- `LanguageManager`: SharedPreferences 기반 언어 설정 영속화
- HTML i18n: `data-i18n` / `data-i18n-html` 속성 + `setLanguage()` JS API
- Android resource 번역: `values-en/strings.xml`, `values-ja/strings.xml`
- 상단 앱 바 (앱 이름 + 언어 선택 버튼 KO/EN/JA)

## [1.0.0] - 2026-03-20

### Added
- 복리저축 계산기: 초기 투자금, 월 납입금, 수익률, 기간, 복리 주기, 세금 반영
- 복리저축 계산기: 자산 성장 차트 (Chart.js Line), 연도별 상세 테이블
- 퇴직 후 인출 계산기: 정액 / 정률 / SWR 3가지 인출 방식
- 퇴직 후 인출 계산기: 자산 소진 시점 분석, 잔액 추이 차트, 시나리오 3종 비교
- BottomNavigationView 탭 전환 (복리저축 계산기 ↔ 인출 계산기)
- 모바일 최적화 UI (터치 슬라이더, 큰 입력 영역, 스크롤 테이블)
- Adaptive 런처 아이콘
