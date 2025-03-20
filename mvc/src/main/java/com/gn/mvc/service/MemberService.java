package com.gn.mvc.service;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
	private final DataSource dataSource;
	private final UserDetailsService userDetailsService;
	
	public int deleteMember(Long id) {
		int result = 0;
		try {
			Member target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username= ?";
				jdbcTemplate.update(sql,target.getMemberId());
				SecurityContextHolder.getContext().setAuthentication(null);
			}			
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public int updatMember(MemberDto param) {
		int result = 0;
		try {
			// 데이터 베이스에 회원 정보 수정
			param.setMember_pw(passwordEncoder.encode(param.getMember_pw()));
			Member update = repository.save(param.toEntity());
			if(update!= null) {
				// remember-me(**DB**, cookie) 가 있다면 무효화 
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username= ?";
				jdbcTemplate.update(sql,param.getMember_id());
				// 변경된 회원 정보 Security Content에 즉시 반영
				UserDetails updateUserDetails = 
						userDetailsService.loadUserByUsername(param.getMember_id());
				Authentication newAuth  = new UsernamePasswordAuthenticationToken(
						updateUserDetails,
						updateUserDetails.getPassword(),
						updateUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
				
				result =1;
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public Member selectMemberOne(Long id) {
		return repository.findById(id).orElse(null);
	}
		
	public MemberDto createMember(MemberDto dto) {
		dto.setMember_pw(passwordEncoder.encode(dto.getMember_pw()));
		
		Member param = dto.toEntity(); // 멤버 디티오에 사용자가 입력한 값 담아서 그냥 멤버로 전해준다 (앤티티로 바꾸겟다)
		
		Member result = repository.save(param); // 바뀐애를 인서트 
		
		
		return new MemberDto().toDto(result);
	}
}
