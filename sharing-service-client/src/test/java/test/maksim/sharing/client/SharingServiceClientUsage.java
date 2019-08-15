package test.maksim.sharing.client;

import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.common.dto.SharingRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SharingServiceClientUsage {

    private static final String DOC_ID_1 = "docId1";
    private static final String DOC_ID_2 = "docId2";
    private static final long SHAREE_ID_1 = 1;
    private static final long SHAREE_ID_2 = 2;
    private static final long SHEET_ID_1 = 11;
    private static final long SHEET_ID_2 = 12;
    private static final long CELL_ID_1 = 111;
    private static final long CELL_ID_2 = 112;

    public static void main(String[] args) {
        SharingServiceClientFactory factory = new SharingServiceClientFactory();
        SharingServiceClient client = factory.defaultClient("http://localhost:8080");

        // share whole doc
        share(client, createRequest(DOC_ID_1, Set.of(SHAREE_ID_1, SHAREE_ID_2), Map.of()));
        load(client, DOC_ID_1, Set.of(SHAREE_ID_1));
        unshare(client, createRequest(DOC_ID_1, Set.of(SHAREE_ID_1), Map.of()));
        load(client, DOC_ID_1, Set.of(SHAREE_ID_1));
        load(client, DOC_ID_1, Set.of(SHAREE_ID_2));

        // share sheet and cells
        Map<Long, List<Long>> sheetCellIds = Map.of(
                SHEET_ID_1, List.of(CELL_ID_1, CELL_ID_2),
                SHEET_ID_2, List.of()
        );
        share(client, createRequest(DOC_ID_2, Set.of(SHAREE_ID_1, SHAREE_ID_2), sheetCellIds));
        load(client, DOC_ID_2, Set.of(SHAREE_ID_1));
        unshare(client, createRequest(DOC_ID_2, Set.of(SHAREE_ID_1), Map.of()));
        load(client, DOC_ID_2, Set.of(SHAREE_ID_1));
    }

    private static void share(SharingServiceClient client,
                              SharingRequest request) {
        client.share(request).join();
    }

    private static void unshare(SharingServiceClient client,
                                SharingRequest request) {
        client.unshare(request).join();
    }

    private static void load(SharingServiceClient client,
                             String docId,
                             Set<Long> shareeIds) {
        Optional<SharedDocument> document = client.load(docId, shareeIds).join();
        System.out.println(document);
    }

    private static SharingRequest createRequest(String docId,
                                                Set<Long> shareeIds, Map<Long, List<Long>> sheetCellIds) {
        var request = new SharingRequest();
        request.setDocumentId(docId);
        request.setRecipientShareeIds(shareeIds);
        request.setSheetCellIds(sheetCellIds);

        return request;
    }
}
