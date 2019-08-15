package test.maksim.sharing.ws.service;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.builder.SharedItemsBuilder;
import test.maksim.sharing.ws.repository.SharingRepository;
import test.maksim.sharing.ws.repository.domain.SharedItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SharingUpdateServiceTest {

    private static final String DOC_ID = "docId";
    private static final long SHEET_ID_1 = 1;
    private static final long SHEET_ID_2 = 2;
    private static final long CELL_ID_1 = 11;
    private static final long CELL_ID_2 = 12;
    private static final Set<Long> RECIPIENT_SHAREE_IDS = Set.of(111L);

    @InjectMocks
    private SharingUpdateService service;

    @Mock
    private SharingRepository sharingRepository;
    @Mock
    private SharedItemsBuilder sharedItemsBuilder;

    @Before
    public void setUp() {
        service = new SharingUpdateService(sharingRepository, List.of(sharedItemsBuilder));
    }

    @Test
    public void share_shouldCallItemsBuilderAndTheCallRepositoryToSave() {
        var sharedItem = mock(SharedItem.class);
        when(sharedItemsBuilder.isApplicable(any())).thenReturn(true);
        when(sharedItemsBuilder.build(any())).thenReturn(Stream.of(sharedItem));

        service.share(createRequest());

        verify(sharedItemsBuilder).build(createRequest());
        verify(sharingRepository).save(List.of(sharedItem));
    }

    @Test
    public void unshare_sheetCellIdsEmpty_shouldUnshareWholeDoc() {
        service.unshare(createRequest());

        verify(sharingRepository).deleteDocument(DOC_ID, RECIPIENT_SHAREE_IDS);
        verifyDeleteSheetsNeverCalled();
        verifyDeleteCellsNeverCalled();
    }

    @Test
    public void unshare_singleSheetId_shouldUnshare1Sheet() {
        service.unshare(createRequest(Map.of(SHEET_ID_1, List.of())));

        verify(sharingRepository).deleteSheets(DOC_ID, List.of(SHEET_ID_1), RECIPIENT_SHAREE_IDS);
        verifyDeleteDocumentNeverCalled();
        verifyDeleteCellsNeverCalled();
    }

    @Test
    public void unshare_singleCellId_shouldUnshare1Cell() {
        service.unshare(createRequest(Map.of(SHEET_ID_1, List.of(CELL_ID_1))));

        verify(sharingRepository).deleteCells(DOC_ID, List.of(CELL_ID_1), RECIPIENT_SHAREE_IDS);
        verifyDeleteDocumentNeverCalled();
        verifyDeleteSheetsNeverCalled();
    }

    @Test
    public void unshare_singleSheetIdAnd2CellIds_shouldUnshare1SheetAnd2Cells() {
        Map<Long, List<Long>> sheetCellIds = Map.of(
                SHEET_ID_1, List.of(CELL_ID_1, CELL_ID_2),
                SHEET_ID_2, List.of()
        );
        service.unshare(createRequest(sheetCellIds));

        verify(sharingRepository).deleteCells(DOC_ID, List.of(CELL_ID_1, CELL_ID_2), RECIPIENT_SHAREE_IDS);
        verify(sharingRepository).deleteSheets(DOC_ID, List.of(SHEET_ID_2), RECIPIENT_SHAREE_IDS);
        verifyDeleteDocumentNeverCalled();
    }

    // Util methods

    private SharingRequest createRequest(Map<Long, List<Long>> sheetCellIds) {
        var request = new SharingRequest();
        request.setDocumentId(DOC_ID);
        request.setRecipientShareeIds(RECIPIENT_SHAREE_IDS);
        request.setSheetCellIds(sheetCellIds);

        return request;
    }

    private SharingRequest createRequest() {
        return createRequest(Map.of());
    }

    private void verifyDeleteDocumentNeverCalled() {
        verify(sharingRepository, never()).deleteDocument(anyString(), anyCollection());
    }

    private void verifyDeleteSheetsNeverCalled() {
        verify(sharingRepository, never()).deleteSheets(anyString(), anyCollection(), anyCollection());
    }

    private void verifyDeleteCellsNeverCalled() {
        verify(sharingRepository, never()).deleteCells(anyString(), anyCollection(), anyCollection());
    }
}