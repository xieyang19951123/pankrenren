package io.renren.modules.pank.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShowEntity {

    private Integer value;

    private String name;

    private ArrayList<String> keys ;

    private List<Integer> vals;
}
