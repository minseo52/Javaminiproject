package tetris;

import javax.swing.*; // GUI 컴포넌트
import java.awt.*; // 그래픽 처리
import java.awt.event.*; // 이벤트 처리
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class TetrisGame extends JPanel implements KeyListener, Runnable {
    private static final int ROWS = 20; // 보드 행 수
    private static final int COLS = 10; // 보드 열 수
    private static final int BLOCK_SIZE = 30; // 블록 크기 (픽셀 단위)

    private Thread gameThread; // 게임 루프 쓰레드
    private volatile boolean running; // 게임 실행 여부
    private volatile boolean paused = false; // 일시 정지 여부

    private Tetromino current; // 현재 조각
    private Tetromino next; // 다음 조각
    private final Color[][] board = new Color[ROWS][COLS]; // 보드 상태 (색상 저장)

    private int score; // 현재 점수
    private List<Integer> rankings = new ArrayList<>(); // 상위 3개 점수 저장

    private JButton pauseButton; // 일시 정지 버튼

    public TetrisGame() {
        setPreferredSize(new Dimension(COLS * BLOCK_SIZE + 200, ROWS * BLOCK_SIZE)); // 게임 창 크기 설정
        setBackground(Color.BLACK); // 배경색 설정
        setFocusable(true); // 키 입력 받기 위해 포커스 설정
        addKeyListener(this); // 키 이벤트 리스너 등록
        initGame(); // 게임 초기화
        createPauseButton(); // 일시 정지 버튼 생성
    }

    private void createPauseButton() {
        pauseButton = new JButton("Pause");
        pauseButton.setBounds(COLS * BLOCK_SIZE + 50, ROWS * BLOCK_SIZE - 60, 100, 40);
        pauseButton.addActionListener(e -> togglePause());
        this.setLayout(null); // 절대 위치 사용
        this.add(pauseButton);
    }

    private void togglePause() {
        paused = !paused; // 일시 정지 상태 토글
        pauseButton.setText(paused ? "Resume" : "Pause"); // 버튼 텍스트 변경

        if (!paused) {
            requestFocusInWindow(); // 다시 시작 시 키 입력을 받을 수 있도록 포커스 요청
        }
    }

    private void initGame() {
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(board[r], null); // 보드 초기화
        }
        score = 0;

        current = Tetromino.random(); // 현재 조각 랜덤 생성
        next = Tetromino.random(); // 다음 조각 랜덤 생성

        start(); // 게임 시작
    }

    private void start() {
        running = true;
        gameThread = new Thread(this, "GameThread"); // 쓰레드 생성
        gameThread.start(); // 쓰레드 시작
    }

    private void gameOver() {
        try {
            running = false;
            rankings.add(score); // 점수 기록
            rankings.sort(Collections.reverseOrder()); // 내림차순 정렬
            if (rankings.size() > 3) {
                rankings = new ArrayList<>(rankings.subList(0, 3)); // 상위 3개만 유지
            }

            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Game Over! Your score: " + score, // 점수 출력
                    "Game Over",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Restart"}, // 재시작 버튼
                    "Restart"
            );
            if (choice == 0) {
                initGame(); // 재시작
            } else {
                System.exit(0); // 종료
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during game over.");
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(500); // 0.5초 간격으로 떨어짐

                if (!paused && !moveDown()) { // 일시 정지 상태가 아니고 더 이상 못 내려가면
                    gameOver(); // 게임 오버
                    break;
                }
                repaint(); // 화면 갱신
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean moveDown() {
        if (current.canMove(1, 0, board)) { // 아래로 이동 가능하면
            current.move(1, 0);
            return true;
        } else {
            lockPiece(); // 현재 조각 고정
            clearFullRows(); // 줄 삭제
            current = next; // 다음 조각 가져오기
            next = Tetromino.random(); // 새 조각 설정
            return current.canMove(0, 0, board); // 새 조각이 배치 가능한지 확인
        }
    }

    private void lockPiece() {
        for (Point p : current.getPoints()) {
            board[p.y][p.x] = current.getColor(); // 보드에 색상 저장
            score += 10; // 고정 시 점수 증가
        }
    }

    private void clearFullRows() {
        for (int r = ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == null) {
                    full = false;
                    break;
                }
            }
            if (full) { // 한 줄이 다 찼으면
                for (int rr = r; rr > 0; rr--) {
                    board[rr] = Arrays.copyOf(board[rr - 1], COLS); // 위 줄 복사
                }
                board[0] = new Color[COLS]; // 최상단은 비우기
                r++; // 삭제한 줄 다시 검사
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 격자 그리기
        g.setColor(Color.DARK_GRAY);
        for (int r = 0; r <= ROWS; r++) {
            g.drawLine(0, r * BLOCK_SIZE, COLS * BLOCK_SIZE, r * BLOCK_SIZE);
        }
        for (int c = 0; c <= COLS; c++) {
            g.drawLine(c * BLOCK_SIZE, 0, c * BLOCK_SIZE, ROWS * BLOCK_SIZE);
        }

        // 고정된 블록 그리기
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] != null) {
                    g.setColor(board[r][c]);
                    g.fillRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    // 회색 선 추가 (격자)
                    g.setColor(Color.GRAY);
                    g.drawRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // 현재 조각 그리기
        for (Point p : current.getPoints()) {
            g.setColor(current.getColor());
            g.fillRect(p.x * BLOCK_SIZE, p.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            // 회색 선 추가 (격자)
            g.setColor(Color.GRAY);
            g.drawRect(p.x * BLOCK_SIZE, p.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }

        // 오른쪽 정보 패널: 다음 조각
        g.setColor(Color.WHITE);
        g.drawString("Next:", COLS * BLOCK_SIZE + 20, 30);
        for (Point p : next.getPoints()) {
            g.setColor(next.getColor());
            g.fillRect(
                    COLS * BLOCK_SIZE + 20 + p.x * BLOCK_SIZE / 2,
                    40 + p.y * BLOCK_SIZE / 2,
                    BLOCK_SIZE / 2,
                    BLOCK_SIZE / 2
            );
        }

        // 점수 및 순위 표시
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, COLS * BLOCK_SIZE + 20, 120);
        g.drawString("Top 3:", COLS * BLOCK_SIZE + 20, 150);
        for (int i = 0; i < rankings.size(); i++) {
            g.drawString((i + 1) + ". " + rankings.get(i), COLS * BLOCK_SIZE + 20, 170 + i * 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (paused) {
            return; // 일시 정지 상태에서 키 입력을 무시
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (current.canMove(0, -1, board)) current.move(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
                if (current.canMove(0, 1, board)) current.move(0, 1);
                break;
            case KeyEvent.VK_DOWN:
                moveDown();
                break;
            case KeyEvent.VK_UP:
                if (current.canRotate(board)) current.rotate();
                break;
            case KeyEvent.VK_SPACE:
                while (moveDown());
                break;
        }
        repaint(); // 조작 후 화면 갱신
    }

    @Override public void keyReleased(KeyEvent e) {} // 사용 안 함
    @Override public void keyTyped(KeyEvent e) {} // 사용 안 함

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris"); // 창 생성
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new TetrisGame()); // 게임 화면 추가
            frame.pack(); // 크기 맞추기
            frame.setVisible(true); // 창 보이기
            frame.setLocationRelativeTo(null);
        });
    }
}