package com.liepin.contract.entity.vo.reqvo;

import com.liepin.contract.entity.vo.list.NewContractListVO;
import lombok.Data;

import java.util.List;

@Data
public class NewContractReqVO {
    private List<NewContractListVO> list;
}
