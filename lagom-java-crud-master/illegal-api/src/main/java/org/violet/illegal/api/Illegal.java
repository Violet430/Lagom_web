package org.violet.illegal.api;

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
public class Illegal {
    /**
     * 违法记录编号
     * 车牌号
     * 号牌种类
     * 机动车所有人
     * 违章驾驶员
     * 驾驶证号
     * 违法时间
     * 违法地点
     * 违法行为
     * 处罚结果
     * 处罚金额
     * 查处机关
     * 执勤民警
     * 决定书号
     * 是否处理
     */
    String id;
    String carNum;
    String plateType;
    String owner;
    String driver;
    String licenseId;
    String time;
    String place;
    String illegalAct;
    String punish;
    String fine;
    String office;
    String police;
    String docId;
    String flag;

}
