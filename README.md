# Javaminiproject

# 🎮 Java Tetris Game

Java와 Swing을 활용한 GUI 테트리스 게임입니다.  
기본 블록 이동부터 점수, 난이도 증가, 게임 오버 및 리스타트까지 포함된 프로젝트입니다.

---

## 📌 프로젝트 설명

클래스를 나누고, 이벤트 기반으로 블록의 움직임을 구현하며, UI도 Swing으로 시각화된 테트리스 게임입니다.  
이 프로젝트는 Java의 객체지향적인 설계를 기반으로 하며, 게임 내 모든 기능은 명확하게 분리되어 관리됩니다.

---

## 플로우차트

![image](https://github.com/user-attachments/assets/b6ea7932-bd04-487a-ad9d-2bb82ff961cf)


## 💡 주요 기능 및 흐름

아래는 게임의 주요 기능 흐름을 나타낸 코드와 해당 로직에 대한 설명입니다:


// 1. 블록이 아래로 떨어짐
block.moveDown();
if (collision()) {
    fixToBoard();
    clearLines();
    spawnNewBlock();
}
``` | - 블록은 일정 시간 간격으로 아래로 내려옴  
- 충돌이 감지되면 보드에 고정  
- 줄을 확인하고 지운 뒤 새 블록 생성 |
| ```java
// 2. 키 입력 처리
switch (keyCode) {
    case KeyEvent.VK_LEFT: block.moveLeft(); break;
    case KeyEvent.VK_RIGHT: block.moveRight(); break;
    case KeyEvent.VK_DOWN: block.moveDown(); break;
    case KeyEvent.VK_UP: block.rotate(); break;
    case KeyEvent.VK_SPACE: block.hardDrop(); break;
}
``` | - 방향키 입력에 따라 블록 이동 및 회전  
- 스페이스바로 하드 드롭(즉시 낙하) 수행 |
| ```java
// 3. 라인 클리어 처리
for (int row = 0; row < board.length; row++) {
    if (isFull(row)) {
        clearRow(row);
        shiftDown(row);
        score += 100;
    }
}
``` | - 한 줄이 모두 차면 제거하고 위 줄을 아래로 내림  
- 점수 100점 추가 |
| ```java
// 4. 난이도 증가 타이머
if (elapsedTime > threshold) {
    speedUp();
    threshold += 30_000; // 30초마다 증가
}
``` | - 시간이 지남에 따라 블록 속도 증가  
- 난이도는 30초마다 한 단계씩 상승 |
| ```java
// 5. 게임 오버
if (block.isAtTop() && collision()) {
    gameOver = true;
    showRestartButton();
}
``` | - 새 블록이 생성될 때 충돌이 발생하면 게임 오버  
- 점수 표시와 함께 재시작 버튼 표시 |

---

## 🛠️ 개발 환경

- **언어**: Java (JDK 11 이상)
- **라이브러리**: Java Swing
- **IDE**: Eclipse, IntelliJ 등
- **플랫폼**: Windows / macOS / Linux

---

## 🖥️ 게임 UI 예시

> 이미지 또는 GIF 첨부 위치 (예: `/assets/screenshot.png`)

