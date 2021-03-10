package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dto.DataSourceDTO;
import com.rigai.rigeye.common.model.DataSourceCluster;
import com.rigai.rigeye.common.service.DataSourceClusterService;
import com.github.pagehelper.PageInfo;
import com.rigai.rigeye.common.model.DataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.rigai.rigeye.common.service.DataSourceService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChenXing
 * @date 2018/08/01
 */
@Service("dataSourceService")
public class DataSourceServiceImpl extends BaseBussinessImpl<DataSource> implements DataSourceService {

	@Resource
	DataSourceClusterService dataSourceClusterService;

	@Override
	public DataSourceDTO getDataSourceDTOById(Long id){
		DataSource dataSource=this.getById(id);
		if(dataSource==null||dataSource.getClusterId()==null){
			return null;
		}
		DataSourceCluster cluster= dataSourceClusterService.getById(dataSource.getClusterId());
		DataSourceDTO dataSourceDTO=new DataSourceDTO();
		BeanUtils.copyProperties(dataSource,dataSourceDTO);
		dataSourceDTO.setClusterIp(cluster.getClusterIp());
		dataSourceDTO.setClusterName(cluster.getClusterName());
		return dataSourceDTO;
	}

	@Override
	public PageInfo<DataSourceDTO> pageGetDataSourceDTO(DataSource param, Integer page, Integer pageSize) {
		PageInfo<DataSource> dataSourcePageInfo=this.pageGet(param,page,pageSize);
		if(dataSourcePageInfo==null||dataSourcePageInfo.getList().size()<1){
			return null;
		}
		List<DataSource> dataSources=dataSourcePageInfo.getList();
		List<DataSourceDTO> dataSourceDTOs=parseDTO(dataSources);
		PageInfo<DataSourceDTO> dataSourceDTOPageInfo=new PageInfo<>();
		dataSourceDTOPageInfo.setList(dataSourceDTOs);
		dataSourceDTOPageInfo.setPages(dataSourcePageInfo.getPages());
		dataSourceDTOPageInfo.setTotal(dataSourcePageInfo.getTotal());
		return dataSourceDTOPageInfo;
	}

	@Override
	public List<DataSourceDTO> getDTOByObj(DataSource param) {
		List<DataSource> dataSources=getByObj(param);
		if(dataSources==null){
			return null;
		}
		return parseDTO(dataSources);
	}


	private List<DataSourceDTO> parseDTO(List<DataSource> dataSources){
		List<DataSourceCluster> clusters=dataSourceClusterService.getAll();
		Map<Long,DataSourceCluster> clusterMap=new HashMap<>(clusters.size());
		clusters.parallelStream().forEach(cluster->clusterMap.put(cluster.getClusterId(),cluster));
		ArrayList<DataSourceDTO> dataSourceDTOs=new ArrayList<>(dataSources.size());
		dataSources.parallelStream().forEachOrdered(dataSource -> {
			DataSourceDTO dto=new DataSourceDTO();
			BeanUtils.copyProperties(dataSource,dto);
			DataSourceCluster currentCluster=clusterMap.get(dto.getClusterId());
			dto.setClusterIp(currentCluster.getClusterIp());
			dto.setClusterName(currentCluster.getClusterName());
			dataSourceDTOs.add(dto);
		});
		return dataSourceDTOs;
	}

}
