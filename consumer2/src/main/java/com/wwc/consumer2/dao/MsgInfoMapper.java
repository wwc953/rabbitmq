package com.wwc.consumer2.dao;

import com.wwc.consumer2.bean.MsgInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MsgInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MsgInfo record);

    int insertSelective(MsgInfo record);

    MsgInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgInfo record);

    int updateByPrimaryKey(MsgInfo record);
}