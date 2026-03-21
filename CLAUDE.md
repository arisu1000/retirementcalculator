# CLAUDE.md — 은퇴소득 계산기 Android 앱

이 파일은 Claude Code agent가 이 프로젝트에서 작업할 때 따라야 할 규칙과 컨텍스트를 정의합니다.

---

## 프로젝트 개요

복리저축 계산기와 퇴직 후 인출 계산기를 합친 Android 앱.
UI는 **WebView 기반 HTML/JS**로 구현되어 있으며, Android 레이어(Kotlin)는 최소한으로 유지한다.

- 패키지명: `com.retirementcalculator`
- 최소 SDK: API 26 / 타겟 SDK: API 34
- 지원 언어: 한국어(ko) · English(en) · 日本語(ja)

---

## 빌드 및 실행

```bash
# Debug APK 빌드
./gradlew assembleDebug

# 기기에 바로 설치
./gradlew installDebug

# APK 위치
app/build/outputs/apk/debug/app-debug.apk
```

빌드 전 `local.properties`에 SDK 경로가 설정되어 있어야 한다.

```
sdk.dir=/Users/wcjung/Library/Android/sdk
```

---

## 파일 구조 및 역할

```
app/src/main/
├── assets/
│   ├── compound_calculator.html     # 복리저축 계산기 (다크 테마)
│   └── withdrawal_calculator.html   # 인출 계산기 (라이트 테마)
├── java/com/retirementcalculator/
│   ├── MainActivity.kt              # BottomNav + 언어 선택 버튼
│   ├── CalculatorFragment.kt        # WebView 래퍼, 언어 주입
│   └── LanguageManager.kt           # 언어 설정 저장/로드 (SharedPreferences)
└── res/
    ├── values/strings.xml           # 기본(한국어) 문자열
    ├── values-en/strings.xml        # 영어 문자열
    └── values-ja/strings.xml        # 일본어 문자열
```

---

## 다국어(i18n) 규칙

### Android 문자열 리소스
- 새 UI 문자열을 추가할 때는 반드시 `values/`, `values-en/`, `values-ja/` 세 곳 모두 추가한다.
- `MainActivity.applyLocalizedLabels(lang)`이 `createConfigurationContext`로 동적 적용하므로, Activity 재시작 없이 반영된다.

### HTML i18n 테이블
- 두 HTML 파일 모두 스크립트 상단에 `const i18n = { ko: {...}, en: {...}, ja: {...} }` 테이블이 있다.
- 새 UI 문자열을 추가할 때는 세 언어 모두 키를 추가한다.
- 정적 요소: `data-i18n="키"` (textContent) 또는 `data-i18n-html="키"` (innerHTML) 속성 사용.
- 동적 요소(차트 레이블, 계산 결과 등): `const t = i18n[currentLang]`로 참조.

### 통화 포맷
언어마다 `fmt(n)` / `fmtShort(n)` 함수가 i18n 테이블 안에 내장되어 있다.
통화 기호와 단위가 다르므로 직접 문자열을 만들지 않고 반드시 이 함수를 사용한다.

| 언어 | 기호 | 단위 | 예시 |
|---|---|---|---|
| ko | ₩ | 만원 / 억원 | 1억 2,500만원 |
| en | $ | K / M / B | $12.50M |
| ja | ¥ | 万円 / 億円 | ¥1億2,500万円 |

입력 필드 prefix 업데이트는 `data-currency` 속성 + `applyTranslations()` 내 일괄 처리한다.

### 언어 전환 흐름
```
사용자 탭 → PopupMenu
  → LanguageManager.set(lang)           # SharedPreferences 저장
  → applyLocalizedLabels(lang)          # Android UI (BottomNav, 앱 이름)
  → CalculatorFragment.updateLanguage() # WebView: evaluateJavascript("setLanguage('lang')")
```

---

## 코드 작성 규칙

### Kotlin
- Android 레이어는 **최소한**으로 유지한다. 계산 로직은 HTML/JS에 있으며 Kotlin으로 옮기지 않는다.
- ViewBinding을 사용하고 `findViewById`는 쓰지 않는다.
- `_binding` null 체크 후 WebView 접근 (`pageLoaded` 플래그 활용).

### HTML / JavaScript
- 계산 로직은 기존 `calculate()` 함수 안에 유지한다.
- 차트는 Chart.js (CDN)를 사용한다. 새 차트 라이브러리를 추가하지 않는다.
- 폰트는 Google Fonts CDN을 사용한다 (복리: Noto Serif KR + DM Mono / 인출: Nanum Myeongjo + JetBrains Mono).
- 인터넷 연결 필요 의존성(Chart.js, Google Fonts)이 있으므로 오프라인 지원 변경 시 README에 반드시 안내를 추가한다.

### CSS / 디자인
- 복리저축 계산기: 다크 테마 (`--bg: #0d0f14` 기반).
- 인출 계산기: 라이트/페이퍼 테마 (`--paper: #f5f0e8` 기반).
- 두 계산기의 테마를 서로 섞지 않는다.
- 모바일 터치 최적화: 슬라이더 thumb 크기 최소 22px, 입력 필드 padding 최소 12px.

---

## Git 커밋 규칙

- 커밋 메시지는 **한국어**로 작성한다.
- 접두사 규칙:
  - `feat:` 새 기능
  - `fix:` 버그 수정
  - `improve:` 기존 기능 개선
  - `rename:` 이름 변경
  - `docs:` 문서만 변경
  - `chore:` 빌드/설정 변경
- 커밋 단위: 논리적으로 하나의 변경만 포함. HTML과 Kotlin을 함께 수정한 경우 하나의 커밋으로 묶는다.
- `Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>` 를 커밋 메시지 끝에 포함한다.

### .gitignore 주의
아래 파일은 커밋하지 않는다.
- `local.properties` (SDK 경로는 개발자마다 다름)
- `app/build/` (빌드 산출물)
- `.gradle/`, `.idea/`

---

## 새 기능 추가 시 체크리스트

- [ ] i18n 테이블 3개 언어(ko/en/ja) 모두 키 추가
- [ ] `values/`, `values-en/`, `values-ja/` 문자열 리소스 추가 (Android UI 요소인 경우)
- [ ] 통화 표시가 필요하면 `fmt()` / `fmtShort()` 사용 (직접 단위 문자열 하드코딩 금지)
- [ ] 슬라이더 추가 시 `applyTranslations()`에서 레이블 초기화 코드 추가
- [ ] `./gradlew assembleDebug` 빌드 성공 확인 후 커밋
- [ ] CHANGELOG.md에 변경 내용 추가

---

## 하지 말아야 할 것

- 계산 로직을 Kotlin으로 이식하지 않는다 (HTML/JS에 유지).
- 새 외부 라이브러리를 `app/build.gradle`에 추가할 때는 반드시 사용 이유를 명시한다.
- 디자인 테마(색상, 폰트)를 사용자 요청 없이 임의로 변경하지 않는다.
- `evaluateJavascript` 호출 시 사용자 입력값을 그대로 삽입하지 않는다 (XSS 방지).
- Activity를 재시작(`recreate()`)하여 언어를 적용하지 않는다 — `applyLocalizedLabels()`로 처리한다.
