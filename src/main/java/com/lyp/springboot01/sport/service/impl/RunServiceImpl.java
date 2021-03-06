package com.lyp.springboot01.sport.service.impl;

import com.lyp.springboot01.common.bean.JsonResult;
import com.lyp.springboot01.common.exception.MyException;
import com.lyp.springboot01.common.util.ExcelUtils;
import com.lyp.springboot01.sport.constant.Common;
import com.lyp.springboot01.sport.mapper.RunDetailMapper;
import com.lyp.springboot01.sport.model.BatchRunDetailVO;
import com.lyp.springboot01.sport.model.QueryRunVO;
import com.lyp.springboot01.sport.model.RunDetail;
import com.lyp.springboot01.sport.model.RunDetailVO;
import com.lyp.springboot01.sport.service.RunService;
import com.lyp.springboot01.sport.util.SportUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class RunServiceImpl implements RunService {

  @Autowired
  RunDetailMapper runDetailMapper;

  @Override
  public JsonResult importOneWeekRun(MultipartFile file) {
    if (Objects.isNull(file) || file.isEmpty()) {
      return JsonResult.validFail("导入文件不能为空");
    }

    try {
      List<RunDetailVO> detailVOS = ExcelUtils.readList(file, 1, RunDetailVO.class, Common.RUN_HEADS, Common.RUN_HEAD_ATTRS);
      List<RunDetail> runDetails = SportUtils.convertRunDetail(detailVOS);

      int insertNum = runDetailMapper.batchInsert(runDetails);
      return JsonResult.success("成功导入，条数为：" + insertNum);
    } catch (MyException e) {
      return JsonResult.fail(e.getMessage());
    }
  }

  @Override
  public JsonResult listCurMonth(QueryRunVO queryVO) {
    setBaseInfo(queryVO);

    List<RunDetail> runDetails = runDetailMapper.selectByMonth(queryVO);

    return JsonResult.success("查询成功", runDetails);
  }

  @Override
  public JsonResult addRunDetails(BatchRunDetailVO runDetailVOS) {
    log.info("Begin to process batch run detail vo info.");
    List<RunDetailVO> runDetailVOList = runDetailVOS.getRunDetailVOList();
    try {
      List<RunDetail> runDetails = SportUtils.convertRunDetail(runDetailVOList);
      int insertNum = runDetailMapper.batchInsert(runDetails);
      log.info("Batch add successfully,insert number:{}.", insertNum);
      return JsonResult.success("成功导入，条数为：" + insertNum);
    } catch (MyException e) {
      log.error("Batch add run detail info failed.msg:{}.", e.getMessage());
      return JsonResult.fail(e.getMessage());
    }
  }

  private void setBaseInfo(QueryRunVO queryVO) {
    if (queryVO == null) {
      queryVO = new QueryRunVO();
    }

    LocalDate now = LocalDate.now();
    if (queryVO.getYear() == null) {
      queryVO.setYear(now.getYear());
    }

    if (queryVO.getMonth() == null) {
      queryVO.setMonth(now.getMonthValue());
    }
  }
}
