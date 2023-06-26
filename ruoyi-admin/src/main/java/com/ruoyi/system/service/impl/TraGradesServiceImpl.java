package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TraGradesMapper;
import com.ruoyi.system.domain.TraGrades;
import com.ruoyi.system.service.ITraGradesService;

/**
 * 训练成绩Service业务层处理
 * 
 * @author ldu
 * @date 2023-06-27
 */
@Service
public class TraGradesServiceImpl implements ITraGradesService 
{
    @Autowired
    private TraGradesMapper traGradesMapper;

    /**
     * 查询训练成绩
     * 
     * @param id 训练成绩主键
     * @return 训练成绩
     */
    @Override
    public TraGrades selectTraGradesById(Long id)
    {
        return traGradesMapper.selectTraGradesById(id);
    }

    /**
     * 查询训练成绩列表
     * 
     * @param traGrades 训练成绩
     * @return 训练成绩
     */
    @Override
    public List<TraGrades> selectTraGradesList(TraGrades traGrades)
    {
        return traGradesMapper.selectTraGradesList(traGrades);
    }

    /**
     * 新增训练成绩
     * 
     * @param traGrades 训练成绩
     * @return 结果
     */
    @Override
    public int insertTraGrades(TraGrades traGrades)
    {
        traGrades.setCreateTime(DateUtils.getNowDate());
        return traGradesMapper.insertTraGrades(traGrades);
    }

    /**
     * 修改训练成绩
     * 
     * @param traGrades 训练成绩
     * @return 结果
     */
    @Override
    public int updateTraGrades(TraGrades traGrades)
    {
        return traGradesMapper.updateTraGrades(traGrades);
    }

    /**
     * 批量删除训练成绩
     * 
     * @param ids 需要删除的训练成绩主键
     * @return 结果
     */
    @Override
    public int deleteTraGradesByIds(Long[] ids)
    {
        return traGradesMapper.deleteTraGradesByIds(ids);
    }

    /**
     * 删除训练成绩信息
     * 
     * @param id 训练成绩主键
     * @return 结果
     */
    @Override
    public int deleteTraGradesById(Long id)
    {
        return traGradesMapper.deleteTraGradesById(id);
    }
}
