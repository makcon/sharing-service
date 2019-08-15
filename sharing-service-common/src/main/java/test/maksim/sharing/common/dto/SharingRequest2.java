package test.maksim.sharing.common.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString(exclude = "cells")
public class SharingRequest2 {

    private long ownerShareeId;
    private List<Long> recipientShareeId;

//    private long ownerId;
//    private List<String> recipientEmails;
//    private List<SharableItem> subjects;

    private Map<Long, List<Long>> cells;
}
