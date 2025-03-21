package com.gn.mvc.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
public class ChatMsgDto {

	private Long room_no;
	private Long sender_no;
	private Long receiver_no;
	private String msg_content;
	
	
	
}
