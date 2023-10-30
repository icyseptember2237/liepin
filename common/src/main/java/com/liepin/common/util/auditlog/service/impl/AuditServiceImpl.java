package com.liepin.common.util.auditlog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liepin.common.util.auditlog.entity.Audit;
import com.liepin.common.util.auditlog.mapper.AuditMapper;
import com.liepin.common.util.auditlog.service.AuditService;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl extends ServiceImpl<AuditMapper, Audit> implements AuditService {
}
