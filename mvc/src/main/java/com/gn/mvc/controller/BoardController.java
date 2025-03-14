package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	// 보드 컨트롤러 안에서 로그백을 써서 기록을 남기겠당 ㅋ 
	private Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	// 1. 필드 주입 -> 순환 참조
//	@Autowired
//	BoardService service;
	
	// 2. 메소드(setter) 주입 -> 불변성 보장 X
//	private BoardService service;
//	
//	@Autowired
//	public void setBoardService(BoardService service) {
//		this.service = service;
//	}
	
	// 3. 생성자 주입 + final
	private final BoardService service;
	
//	@Autowired
//	public BoardContoller(BoardService service) {
//		this.service = service;
//	}
	
	
	@GetMapping("/board/create")
	public String CreateBoardViews() {
		return "board/create";
	}
	
	@PostMapping("/board")
	@ResponseBody //응답해주는 형식 
	public Map<String,String> createBoardApi(			
//			@RequestParam("board_title") String boardTitle,
//			@RequestParam("board_content") String boardContent
			
//			@RequestParam Map<String,String> param
			BoardDto dto
	) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 등록 중 오류가 발생하였습니다.");
		
		// Service가 가지고 있는 createBoard 메소드 호출
		BoardDto result =  service.createBoard(dto);
		
		// debug: 디버그 레벨의 로그 제일 낮음 출력하는 느낌 
		logger.debug("1 : " +result.toString());
		logger.info("2 : " +result.toString());
		logger.warn("3 : " +result.toString());
		logger.error("4 : " +result.toString());
		// 2,3,4번만 출력 ->info가 디폴트여서?
		// 개발할때는 debug 출시할때는 info나 warn
		
		return resultMap;
	}
	
	
	//게시글 목록 출력 
	@GetMapping("/board")
	public String selectBoardAll(Model model, SearchDto searchDto) {
		// 1.DB에서 목록 SELECT
		List<Board> resultList = service.selectBoardAll(searchDto);
		// 2. 목록 Model에 등록
		model.addAttribute("boardList",resultList);
		model.addAttribute("searchDto",searchDto);
		// 3. list.html에 데이터 셋팅
		return "board/list";
	}
	
	
	
	
	
}
