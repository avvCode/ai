package com.vv.bi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 */
@Data
public class GenChartByAIRequest implements Serializable {

    /**
     * 图表类型
     */
    private String chartType;
    /**
     * 图表名称
     */
    private String name;
    /**
     * 图表名称
     */
    private String goal;

    private static final long serialVersionUID = 1L;
}