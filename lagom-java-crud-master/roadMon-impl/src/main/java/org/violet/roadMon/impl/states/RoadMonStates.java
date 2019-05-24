package org.violet.roadMon.impl.states;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.violet.roadMon.api.RoadMon;

import java.util.Optional;

@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class RoadMonStates implements CompressedJsonable {
    Optional<RoadMon> roadMon;
    String timestamp;
}
