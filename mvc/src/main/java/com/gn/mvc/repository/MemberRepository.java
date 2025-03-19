package com.gn.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.mvc.entity.Member;

// dao느낌 마지막에 db와 통신하는 친구 
public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Member findBymemberId(String Keyword);
	
}
