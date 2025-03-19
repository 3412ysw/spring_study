package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.service.MemberService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor  // final때문에 들어감 final 필드를 활용한 생성자 
public class MemberController {
	
	private final MemberService service; //값이 1회성 -> 순환참조 발생 ㄴ ㄴ 

	@GetMapping("/login")
	public String loginView() {
		return "member/login";
	}
	
	@GetMapping("/signup")
	public String CreateMemberViews() {
	System.out.println("회원가입 화면전환 ");
		return "member/create";
	}
	
	@PostMapping("/signup")
	@ResponseBody //응답해주는 형식 
	public Map<String,String> createMemberApi(MemberDto dto) {//dto의 키 값기준으로 form 안에 데이터 꺼내올 수 있음
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원가입 중 오류가 발생하였습니다.");
	
		// Service가 가지고 있는 createBoard 메소드 호출
		MemberDto createdMember = service.createMember(dto);
		
		if(createdMember != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원가입 됨 ㄹㅇㅋㅋ");
		}
		
		return resultMap;
	}

}
