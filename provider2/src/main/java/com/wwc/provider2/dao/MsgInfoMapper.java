package com.wwc.provider2.dao;

import com.wwc.provider2.bean.MsgInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MsgInfoMapper {

    int insertSelective(MsgInfo record);

    MsgInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgInfo record);

    List<MsgInfo> queryList(MsgInfo msgInfo);
}