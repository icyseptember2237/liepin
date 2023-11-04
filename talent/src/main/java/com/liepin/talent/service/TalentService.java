package com.liepin.talent.service;

import com.liepin.common.constant.classes.Result;
import com.liepin.talent.entity.vo.req.AddTalentReqVO;
import com.liepin.talent.entity.vo.req.AlterTalentReqVO;
import com.liepin.talent.entity.vo.req.GetTalentListReqVO;
import com.liepin.talent.entity.vo.resp.GetTalentListRespVO;
import com.liepin.talent.entity.vo.resp.ImportTalentRespVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface TalentService {

    Result<GetTalentListRespVO> getTalentList(GetTalentListReqVO reqVO);

    Result<ImportTalentRespVO> importTalent(MultipartFile file);

    Result addTalent(AddTalentReqVO reqVO);

    Result alterTalent(AlterTalentReqVO reqVO);

    Result deleteTalent(Long id);

    Result pullTalent(Long id);
}
