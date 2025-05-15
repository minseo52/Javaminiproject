# Javaminiproject

# 🎮 Java Tetris Game

Java와 Swing을 사용한 테트리스 게임입니다.  
블록 드롭, 회전, 점수 시스템, 난이도 증가, 게임 오버 및 재시작 버튼 등 필수 기능이 포함된 프로젝트입니다.

---

## 📌 프로젝트 설명

객체지향 구조 기반의 자바 테트리스 게임으로, GUI는 Swing으로 구현되었습니다.  
게임의 상태 변화는 타이머와 키 이벤트 기반으로 이루어지며, 점수와 난이도 조절 시스템이 포함되어 있습니다.

---
## FlowChart

![image](https://github.com/user-attachments/assets/79e38404-4259-452d-94b2-f58ba262110d)


---

## 🖥️ 게임 UI 예시

## 게임 시작 화면

![image](https://github.com/user-attachments/assets/0b9ff4e0-6d91-40c0-a6cf-9ea8e0931aa6)

## 게임 종료 화면

![image](https://github.com/user-attachments/assets/168560f3-48ff-4e97-9263-a4813a065d1b)

## 게임 영상

<img src="https://github.com/user-attachments/assets/fd1c5474-57f2-40fa-b5be-9022874a23ef" width="400" />

## 하드 드롭 기능

<img src="https://github.com/user-attachments/assets/ec8e4916-9b1a-49e2-b4df-f179353840c2" width="400" />

---

## 💡 주요 기능 및 코드 흐름

### 1. 블록 낙하 및 충돌 처리

**기능 설명:**  
일정 시간마다 블록이 아래로 내려오며, 땅이나 다른 블록과 충돌하면 고정되고 다음 블록이 생성됩니다.

```java
block.moveDown();
if (collision()) {
    fixToBoard();
    clearLines();
    spawnNewBlock();
}
```

---

### 2. 키보드 입력으로 블록 이동/회전/하드드롭

**기능 설명:**  
방향키 및 스페이스바로 블록을 좌우로 이동하거나 회전할 수 있습니다.  
스페이스바를 누르면 현재 블록이 즉시 바닥까지 떨어지는 하드드롭 기능이 실행됩니다.

```java
switch (keyCode) {
    case KeyEvent.VK_LEFT: block.moveLeft(); break;
    case KeyEvent.VK_RIGHT: block.moveRight(); break;
    case KeyEvent.VK_DOWN: block.moveDown(); break;
    case KeyEvent.VK_UP: block.rotate(); break;
    case KeyEvent.VK_SPACE: block.hardDrop(); break;
}
```

---

### 3. 한 줄 완성 시 삭제 및 점수 증가

**기능 설명:**  
보드의 가로 한 줄이 전부 채워지면 해당 줄이 제거되고 점수가 증가합니다.  
제거된 줄 위에 있던 모든 줄은 한 줄씩 아래로 내려옵니다.

```java
for (int row = 0; row < board.length; row++) {
    if (isFull(row)) {
        clearRow(row);
        shiftDown(row);
        score += 100;
    }
}
```

---

### 4. 경과 시간에 따른 블록 속도 증가 (난이도 상승)

**기능 설명:**  
게임이 진행될수록 난이도가 올라가며 블록이 더 빠르게 낙하합니다.  
일정 시간(예: 30초)마다 낙하 속도를 높여 플레이어에게 도전적인 게임 환경을 제공합니다.

```java
if (elapsedTime > threshold) {
    speedUp();
    threshold += 30000; // 30초마다 속도 증가
}
```

---

### 5. 게임 오버 및 재시작 버튼 표시

**기능 설명:**  
새 블록이 생성되자마자 충돌이 발생하면 게임 오버로 판단합니다.  
게임 오버 시 현재 점수를 표시하고, "Restart" 버튼이 화면에 나타나 게임을 다시 시작할 수 있도록 합니다.

```java
if (block.isAtTop() && collision()) {
    gameOver = true;
    showRestartButton();
}
```

---
