package com.vv.bi.model.vo;

import lombok.Data;

/**
 * @author vv
 * @Description AI返回的数据结果
 * @date 2023/7/8-22:19
 */
@Data
public class AiResponse {
    private String genChart;
    private String genResult;
    private Long chartId;
}
