package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor  // final때문에 들어감 final 필드를 활용한 생성자 
public class MemberController {
	
	private final MemberService service; //값이 1회성 -> 순환참조 발생 ㄴ ㄴ 
	
	@DeleteMapping("/member/{id}")
	@ResponseBody
	public Map<String,String> memberDeleteApi(@PathVariable("id") Long id,
			HttpServletResponse response) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 탈퇴 중 오류가 발생했습니다.");
		
		int result = service.deleteMember(id);
		
		if(result > 0 ) {
			Cookie rememberMe = new Cookie("remember-me",null);
			rememberMe.setMaxAge(0);
			rememberMe.setPath("/");
			response.addCookie(rememberMe);
			resultMap.put("res_code","200");
			resultMap.put("res_msg", "회원 탈퇴에 성공하였습니다.");
		}
		
	    return resultMap;
	}
	
	
	
	@PostMapping("/member/{id}/update")
	@ResponseBody
	public Map<String,String> memberUpateApi(MemberDto param,
			HttpServletResponse response){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 수정 중 오류가 발생했습니다.");
		
		int result = service.updatMember(param);
		if(result>0) {
			// 쿠키(remember-me 무효화)
			Cookie rememberMe = new Cookie("remember-me",null);
			rememberMe.setMaxAge(0);
			rememberMe.setPath("/");
			response.addCookie(rememberMe);
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원 수정이 완료되었습니다.");
		}
		return resultMap;
	}
	
	@GetMapping("/member/{id}/update")
	public String memberUpdateView(@PathVariable("id") Long id, Model model) {
		Member member = service.selectMemberOne(id);
		model.addAttribute("member",member);
		return "member/update";
	}

	@GetMapping("/login")
	public String loginView(
			@RequestParam(value="error", required=false) String error,
			@RequestParam(value="errorMsg", required=false) String errorMsg,
			Model model) {
		model.addAttribute("error", error);
		model.addAttribute("errorMsg", errorMsg);
		
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
