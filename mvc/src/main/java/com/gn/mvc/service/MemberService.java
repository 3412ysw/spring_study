package com.gn.mvc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository repository;
	// 그냥 멤버가 엔티티고 엔티티는 테이블하고 연관이 되어있던 애고 데베 통신에만 사용
	private final PasswordEncoder passwordEncoder;
		
	public MemberDto createMember(MemberDto dto) {
		dto.setMember_pw(passwordEncoder.encode(dto.getMember_pw()));
		
		Member param = dto.toEntity(); // 멤버 디티오에 사용자가 입력한 값 담아서 그냥 멤버로 전해준다 (앤티티로 바꾸겟다)
		
		Member result = repository.save(param); // 바뀐애를 인서트 
		
		
		return new MemberDto().toDto(result);
	}
}
