package com.ubcn.psam.model;

import lombok.Data;

@Data
public class PSamServer {

	public String SERV_IP; //`   VARCHAR(15) NOT NULL COMMENT '서버IP	', -- 서버IP
	public int SERV_PORT;  //` INT         NOT NULL DEFAULT 0 COMMENT '서버포트', -- 서버포트
	public int ADM_PORT;   //`  INT         NOT NULL DEFAULT 0 COMMENT '관리자포트', -- 관리자포트
	public int TOTL_SAM;   //`  INT         NOT NULL DEFAULT 0 COMMENT '전체SAM수', -- 전체SAM수
	public int BUSY_SAM;   //`  INT         NOT NULL DEFAULT 0 COMMENT 'busy SAM수', -- busy SAM수
	public int IDLE_SAM;   //`  INT         NOT NULL DEFAULT 0 COMMENT 'idle SAM수', -- idle SAM수
	public int BAD_SAM;    //`   INT         NOT NULL DEFAULT 0 COMMENT '불량 SAM수', -- 불량 SAM수
}
