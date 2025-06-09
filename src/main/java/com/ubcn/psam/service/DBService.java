package com.ubcn.psam.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubcn.psam.common.packet.PSAMTransData;
import com.ubcn.psam.common.util.StringUtils;
import com.ubcn.psam.mapper.maria.MariaMapper;
import com.ubcn.psam.model.PSamServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DBService {
	
	@Autowired
	private MariaMapper mariadb;
	
	/**
	 * PSAM 서버 정보
	 * @param servNum
	 * @return
	 */
	public PSamServer select_PSAM_server(int servNum) {
		PSamServer psamServ = null;
		Map<String,Object> map = new HashMap<>();
		
		try {
			map.put("servnum", servNum);
			psamServ = mariadb.selectPSAMserv(map);
		}catch(Exception ex) {
			log.error(StringUtils.getPrintStackTrace(ex));
		}
		
		return psamServ;
	}
	
	/**
	 * 
	 * @param serverNum
	 * @return
	 */
	public String GenerateUniqueNo(String serverNum) {
		String rtnUniqueno="";
		try {
			rtnUniqueno = mariadb.GenerateUniqueNo(serverNum);
		}catch(Exception ex) {
			log.error(StringUtils.getPrintStackTrace(ex));			
		}//
		
		return rtnUniqueno;
	}
	
	/**
	 * 
	 * @param transData
	 * @return
	 */
	public int insert_PSAM_TRANINFO(PSAMTransData  transData) {
		int rtnCode = 0;
		
//		log.info("uniqueno:{}", transData.getUniqueno());
//		log.info("terminal_id:{}",transData.getTermId());
//		log.info("serv_name:{}",transData.getServName());
//		log.info("tran_date:{}",transData.getTranDate());
//		log.info("tran_time:{}",transData.getTranTime());
//		log.info("tran_type:{}",transData.getTransType());
//		log.info("msg_type:{}",transData.getMessageType());
//		log.info("input_type:{}",transData.getInputType());
//		log.info("tran_num:{}",transData.getTermTransNo());
//		log.info("res_code:{}",transData.getReplyCode());
//		log.info("van_code:{}",transData.getVanCode());
		
		Map<String,Object> map = new HashMap<>();
		try {
			map.put("uniqueno", transData.getUniqueno());
			map.put("terminal_id",transData.getTermId());
			map.put("serv_name",transData.getServName());
			map.put("tran_date",transData.getTranDate());
			map.put("tran_time",transData.getTranTime());
            map.put("tran_type",transData.getTransType());
			map.put("msg_type",transData.getMessageType());
			map.put("input_type",transData.getInputType());
			map.put("tran_num",transData.getTermTransNo());
			map.put("res_code",transData.getReplyCode());
			map.put("van_code",transData.getVanCode());
			
			rtnCode = mariadb.insert_PSAM_TRANINFO(map);			
		}catch(Exception ex) {
			log.error(StringUtils.getPrintStackTrace(ex));			
		}//		
		return rtnCode;		
	}
	
	/**
	 * 
	 * @param transData
	 * @return
	 */
	public int update_PSAM_TRANINFO(PSAMTransData  transData) {
		int rtnCode = 0;
		Map<String,Object> map = new HashMap<>();
		
//		log.info("serv_num:{}",transData.getSamServNum());
//		log.info("sam_num:{}",transData.getSamNum());
//		log.info("totl_sam:{}",transData.getTotalSam());
//		log.info("busy_sam:{}",transData.getBusySam());
//		log.info("idle_sam:{}",transData.getIdleSam());            
//		log.info("bad_sam:{}",transData.getBadSam());
//        
//		log.info("cert_code:{}",transData.getCertiCode());
//		log.info("sect_no:{}",transData.getSecNum());
//		log.info("chip_snum:{}",transData.getCsn());
//		log.info("auth_key:{}",transData.getAuthNumKey());			
//		log.info("card_key:{}",transData.getCardNumKey());
//		
//		log.info("res_code:{}",transData.getReplyCode());
//		log.info("res_msg:{}",transData.getReplyMessage());
//		
//		log.info("uniqueno:{}", transData.getUniqueno());
		
		
		try {
			
			map.put("serv_num",transData.getSamServNum());
			map.put("sam_num",transData.getSamNum());
			map.put("totl_sam",transData.getTotalSam());
			map.put("busy_sam", transData.getBusySam());
            map.put("idle_sam",transData.getIdleSam());            
            map.put("bad_sam",transData.getBadSam());
            
			map.put("cert_code",transData.getCertiCode());
			map.put("sect_no",transData.getSecNum());
			map.put("chip_snum",transData.getCsn());
			map.put("auth_key",transData.getAuthNumKey());			
			map.put("card_key",transData.getCardNumKey());
			
			map.put("res_code",transData.getReplyCode());
			map.put("res_msg",transData.getReplyMessage());
			
			map.put("uniqueno", transData.getUniqueno());
			
			rtnCode = mariadb.update_PSAM_TRANINFO(map);			
		}catch(Exception ex) {
			log.error(StringUtils.getPrintStackTrace(ex));			
		}//		
		return rtnCode;		
	}
	
	

}
