package com.gn.todo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.TodoRepository;
import com.gn.todo.specification.TodoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

	private final TodoRepository repository;
	
	public int createTodo(TodoDto dto) {
		int result = 0;
		try {
			
			Todo entity = dto.toEntity();
			repository.save(entity);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Page<Todo> selectTodoAll(SearchDto searchDto, PageDto pageDto){
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage() - 1, pageDto.getNumPerPage());
		
		Specification<Todo> spec = (root, query, criteriaBuilder) -> null;
		if(searchDto.getSearch_text()!=null && !searchDto.getSearch_text().isEmpty()) {
			spec = spec.and(TodoSpecification.todoContentContains(searchDto.getSearch_text()));
			
		}
		
		Page<Todo> list = repository.findAll(spec,pageable);
		return list;
	}
	
	public int deleteTodo(Long id) {
		int result = 0;
		try {
			Todo target = repository.findById(id).orElse(null);
			if (target != null) {
				repository.deleteById(id);
			}
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int updateTodo(Long id) {
		int result = 0;
		
		try {
			Todo target = repository.findById(id).orElse(null);
			
			if (target != null) {
				TodoDto dto = new TodoDto().toDto(target);
				if(dto.getFlag().equals("Y")) {
					dto.setFlag("N");
				}else if(dto.getFlag().equals("N")) {
					dto.setFlag("Y");
				}
				repository.save(dto.toEntity());
				result= 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
