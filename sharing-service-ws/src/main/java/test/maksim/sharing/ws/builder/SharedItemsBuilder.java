package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.repository.domain.SharedItem;

import java.util.stream.Stream;

public interface SharedItemsBuilder {

    boolean isApplicable(SharingRequest request);

    Stream<SharedItem> build(SharingRequest request);
}
