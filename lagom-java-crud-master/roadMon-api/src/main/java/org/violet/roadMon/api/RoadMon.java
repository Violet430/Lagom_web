package org.violet.roadMon.api;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.annotation.concurrent.Immutable;

@Value
@Builder
@Immutable
@JsonDeserialize
@AllArgsConstructor
public class RoadMon {
    /**
     * 编号
     * 车牌
     * 时间
     * 地点
     * 违法行为
     * 决定书
     * 处罚结果
     * 处罚金额
     * 图片
     */
    String id;
    String carNum;
    String time;
    String place;
    String illegalAct;
    String docId;
    String punish;
    String fine;
    String pic;
}
