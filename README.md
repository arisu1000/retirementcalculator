# 은퇴소득 계산기 (Retirement Income Calculator)

복리저축 계산기와 퇴직 후 인출 계산기를 합친 Android 앱입니다.

## 화면 구성

### 복리저축 계산기 (Compound Interest Calculator)
- 초기 투자금 / 월 납입금 설정
- 연 수익률, 투자 기간, 복리 주기 (연/분기/월/일) 선택
- 세금·수수료 반영
- 세후 최종 자산, 자산 성장 차트, 연도별 상세 테이블 제공

### 퇴직 후 인출 계산기 (Withdrawal Calculator)
- **정액 인출**: 매달 고정 금액 인출 + 물가상승률 반영
- **정률 인출**: 잔액의 N%를 매년 인출
- **SWR 분석**: 자산 소진 없이 유지 가능한 안전 인출률 자동 계산
- 자산 소진 시점, 잔액 추이 차트, 시나리오 3종 비교

## 기술 스택

| 항목 | 내용 |
|---|---|
| 언어 | Kotlin |
| 최소 SDK | API 26 (Android 8.0 Oreo) |
| 타겟 SDK | API 34 (Android 14) |
| UI 방식 | WebView + BottomNavigationView |
| 차트 | Chart.js 4.4.1 (CDN) |
| 폰트 | Noto Serif KR, DM Mono, Nanum Myeongjo, JetBrains Mono (Google Fonts CDN) |

## 프로젝트 구조

```
retirementcalculator/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   ├── compound_calculator.html     # 복리저축 계산기 (다크 테마)
│       │   └── withdrawal_calculator.html   # 인출 계산기 (라이트 테마)
│       ├── java/com/retirementcalculator/
│       │   ├── MainActivity.kt              # BottomNavigation 진입점
│       │   └── CalculatorFragment.kt        # WebView 래퍼 Fragment
│       └── res/
│           ├── drawable/                    # 벡터 아이콘, 런처 포그라운드
│           ├── layout/                      # activity_main, fragment_calculator
│           ├── menu/                        # bottom_nav_menu
│           ├── mipmap-anydpi-v26/           # Adaptive 런처 아이콘
│           └── values/                      # colors, strings, themes
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## 빌드 및 실행

### 요구사항
- Android Studio Hedgehog (2023.1.1) 이상
- Android SDK Platform 34
- JDK 17

### Android Studio에서 열기

1. Android Studio 실행
2. **File → Open** → 이 폴더 선택
3. Gradle 동기화 완료 대기
4. Run 버튼 (▶) 또는 `Shift+F10`

### 커맨드라인 빌드

```bash
# Debug APK 빌드
./gradlew assembleDebug

# 기기에 바로 설치
./gradlew installDebug

# APK 위치
# app/build/outputs/apk/debug/app-debug.apk
```

### ADB 직접 설치

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 오프라인 지원 (선택)

현재 Chart.js와 Google Fonts는 CDN에서 로드됩니다.
인터넷 연결 없이도 동작하게 하려면:

1. [Chart.js](https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js)를 다운로드하여 `app/src/main/assets/chart.umd.min.js`로 저장
2. 두 HTML 파일의 `<script src="https://cdnjs...">` 부분을 아래와 같이 수정:
   ```html
   <script src="chart.umd.min.js"></script>
   ```
3. Google Fonts `<link>` 태그를 제거하면 시스템 기본 폰트로 폴백

## 라이선스

MIT
