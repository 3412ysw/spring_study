package com.gn.todo.contoller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoController {

	private final TodoService service;
	
	
	@GetMapping({"","/","/todo"})
	public String selectBoardAll(Model model, SearchDto searchDto, PageDto pageDto) {
		
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		
		Page<Todo> resultList = service.selectTodoAll(searchDto,pageDto);
		
		pageDto.setTotalPage(resultList.getTotalPages());
		
		model.addAttribute("todoList",resultList);
		model.addAttribute("searchDto",searchDto);
		model.addAttribute("pageDto",pageDto);
		
		return "home";
	}
	
	
	
	@PostMapping("/create")
	@ResponseBody
	public Map<String,String> createTodoApi(TodoDto dto) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "등록 중 오류가 발생하였습니다.");
		
		int result = service.createTodo(dto);
		if(result>0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "등록이 완료되었습니다.");
		}
		
		return resultMap;
	}
	
	@DeleteMapping("/todo/{id}")
	@ResponseBody
	public Map<String,String> deleteBoardApi(@PathVariable("id") Long id){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "삭제 중 오류가 발생하였습니다.");
		
		int result = service.deleteTodo(id);
		
		if(result > 0 ) {
			resultMap.put("res_code","200");
			resultMap.put("res_msg", "삭제에 성공하였습니다.");
		}
		return resultMap;
	}
	
	@PostMapping("/todo/{id}/update")
	@ResponseBody
	public Map<String,String> updateBoardApi(@PathVariable("id") Long id) {
		
		System.out.println(id);
		
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 수정 중 오류가 발생하였습니다.");
		
		int result = service.updateTodo(id);
	
		if(result>0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 수정되었습니다.");
		}

		return resultMap;
	}
	
}
