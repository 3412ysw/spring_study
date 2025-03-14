package com.gn.mvc.dto;

import java.time.LocalDateTime;

import com.gn.mvc.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SearchDto {

	private int search_type;
	private String search_text; //html name 정보와 일치
}
