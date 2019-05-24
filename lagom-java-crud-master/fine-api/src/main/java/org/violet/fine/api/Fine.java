package org.violet.fine.api;


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
public class Fine {
    /**
     * 编号
     * 驾驶员
     * 号牌
     * 违法记录编号
     * 决定书号
     * 罚款金额
     * 滞纳金
     * 受理时间
     * 受理机构
     */
    String id;
    String driver;
    String carNum;
    String illegalId;
    String docID;
    String fine;
    String forfeit;
    String time;
    String office;
}
