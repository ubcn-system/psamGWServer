package com.ubcn.psam.mapper.maria;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ubcn.psam.model.PSamServer;

@Mapper
public interface MariaMapper {
	
	public PSamServer selectPSAMserv(Map<String,Object> param);
	
	public List<PSamServer> selectPSAMservList();
	
	public String GenerateUniqueNo(String serverNum);
	
	public int insert_PSAM_TRANINFO(Map<String,Object> param);
	
	public int update_PSAM_TRANINFO(Map<String,Object> param);
	
	public int update_PSAM_SERVER(Map<String,Object> param);
}
