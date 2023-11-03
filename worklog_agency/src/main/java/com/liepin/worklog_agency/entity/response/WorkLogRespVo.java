package com.liepin.worklog_agency.entity.response;

import com.baomidou.mybatisplus.annotation.TableId;
import com.liepin.worklog_agency.entity.base.WorkLogDetail;
import com.liepin.worklog_agency.entity.base.WorkLogProblem;
import com.liepin.worklog_agency.entity.request.InsertProblemLogReqVO;
import lombok.Data;

import java.util.List;
@Data
public class WorkLogRespVo extends WorkLogDetail {

    @TableId
    private Long id;
    private Integer wechatNum;
    private Integer phoneNum;
    private Integer intentionalCustomer;
    private String workSituation;
    private String workSummary;
    private String workPlan;
    private List<InsertProblemLogReqVO> insertProblemLogReqVOList ;
}
