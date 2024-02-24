package com.liepin.common.util.talentBasicConfig.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Node {
    private String value;
    private List<Node> children;

    public Node(){
        children = new ArrayList<>();
    }
}

