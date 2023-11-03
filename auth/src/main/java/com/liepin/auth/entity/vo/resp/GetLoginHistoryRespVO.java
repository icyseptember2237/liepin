package com.liepin.auth.entity.vo.resp;

import com.liepin.auth.loginlog.entity.SysLog;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetLoginHistoryRespVO {
    private List<SysLog> list;
    private Long total;
}
