package com.spLogin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// 단순 DTO 정의
class Board {
  private Long id;
  private String title;
  private String content;

  public Board(Long id, String title, String content) {
    this.id = id;
    this.title = title;
    this.content = content;
  }

  public Long getId() { return id; }
  public String getTitle() { return title; }
  public String getContent() { return content; }
}

// BoardService 단순 구현
class BoardService {
  private final List<Board> boards = new ArrayList<>();
  private long sequence = 1;

  public Board createBoard(String title, String content) {
    Board board = new Board(sequence++, title, content);
    boards.add(board);
    return board;
  }

  public Board getBoard(Long id) {
    return boards.stream()
        .filter(b -> b.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
  }

  public List<Board> getAllBoards() {
    return new ArrayList<>(boards);
  }
}

// 테스트 코드
class BoardServiceTest {

  private BoardService boardService;

  @BeforeEach
  void setUp() {
    boardService = new BoardService();
  }

  @Test
  @DisplayName("게시글 생성 테스트")
  void createBoard_test() {
    Board board = boardService.createBoard("제목1", "내용1");

    assertThat(board.getId()).isEqualTo(1L);
    assertThat(board.getTitle()).isEqualTo("제목1");
    assertThat(board.getContent()).isEqualTo("내용1");
  }

  @Test
  @DisplayName("게시글 단건 조회 성공")
  void getBoard_success() {
    boardService.createBoard("제목1", "내용1");
    Board board = boardService.getBoard(1L);

    assertThat(board.getTitle()).isEqualTo("제목1");
  }

  @Test
  @DisplayName("게시글 단건 조회 실패 - 존재하지 않는 ID")
  void getBoard_fail() {
    assertThatThrownBy(() -> boardService.getBoard(999L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("게시글이 없습니다.");
  }

  @Test
  @DisplayName("게시글 전체 조회 테스트")
  void getAllBoards_test() {
    boardService.createBoard("제목1", "내용1");
    boardService.createBoard("제목2", "내용2");

    assertThat(boardService.getAllBoards().size()).isEqualTo(2);
  }
}
