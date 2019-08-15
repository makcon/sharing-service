package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.ws.repository.domain.StoredSharedItem;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SharedDocumentBuilderTest {

    private static final String DOC_ID = "docId";
    private static final long SHEET_ID_1 = 1;
    private static final long SHEET_ID_2 = 2;
    private static final long CELL_ID_1 = 11;
    private static final long CELL_ID_2 = 12;

    private final SharedDocumentBuilder builder = new SharedDocumentBuilder();

    @Test
    public void build_itemsEmpty_shouldReturnEmpty() {
        Optional<SharedDocument> document = builder.build(Set.of());

        assertThat(document.isEmpty(), is(true));
    }

    @Test
    public void build_singleItemWithEmptySheetId_shouldReturnSharedWholeDoc() {
        Optional<SharedDocument> document = builder.build(Set.of(createSharedItem()));

        assertThat(document.isPresent(), is(true));
        assertThat(document.get(), equalTo(createSharedDoc(Map.of())));
    }

    @Test
    public void build_singleItemWithNonEmptySheetId_shouldReturnShared1Sheet() {
        Optional<SharedDocument> document = builder.build(Set.of(createSharedItem(SHEET_ID_1)));

        assertThat(document.isPresent(), is(true));
        assertThat(document.get(), equalTo(createSharedDoc(Map.of(SHEET_ID_1, List.of()))));
    }

    @Test
    public void build_2ItemsWithNonEmptyCellIds_shouldReturnShared2Sheet() {
        Set<StoredSharedItem> items = Set.of(createSharedItem(SHEET_ID_1), createSharedItem(SHEET_ID_2));

        Optional<SharedDocument> document = builder.build(items);

        assertThat(document.isPresent(), is(true));
        Map<Long, List<Long>> sheetCellIds = Map.of(
                SHEET_ID_1, List.of(),
                SHEET_ID_2, List.of()
        );
        assertThat(document.get(), equalTo(createSharedDoc(sheetCellIds)));
    }

    @Test
    public void build_2ItemsOneWithSingleCellId_shouldReturnSharedOneSheetAndSecond1Cell() {
        Set<StoredSharedItem> items = Set.of(createSharedItem(SHEET_ID_1), createSharedItem(SHEET_ID_2, CELL_ID_1));

        Optional<SharedDocument> document = builder.build(items);

        assertThat(document.isPresent(), is(true));
        Map<Long, List<Long>> sheetCellIds = Map.of(
                SHEET_ID_1, List.of(),
                SHEET_ID_2, List.of(CELL_ID_1)
        );
        assertThat(document.get(), equalTo(createSharedDoc(sheetCellIds)));
    }

    @Test
    public void build_2ItemsOneWith2CellIds_shouldReturnSharedOneSheetAndSecond2Cells() {
        Set<StoredSharedItem> items = Set.of(
                createSharedItem(SHEET_ID_1),
                createSharedItem(SHEET_ID_2, CELL_ID_1),
                createSharedItem(SHEET_ID_2, CELL_ID_2)
        );

        Optional<SharedDocument> document = builder.build(items);

        assertThat(document.isPresent(), is(true));
        assertThat(document.get().getDocumentId(), equalTo(DOC_ID));
        assertThat(document.get().getSheetCellIds().get(SHEET_ID_1).isEmpty(), is(true));
        assertThat(document.get().getSheetCellIds().get(SHEET_ID_2), containsInAnyOrder(CELL_ID_1, CELL_ID_2));
    }

    // Util methods

    private StoredSharedItem createSharedItem(long sheetId,
                                        long cellId) {
        return StoredSharedItem.builder()
                .cellId(cellId)
                .sheetId(sheetId)
                .documentId(DOC_ID)
                .build();
    }

    private StoredSharedItem createSharedItem(long sheetId) {
        return StoredSharedItem.builder()
                .sheetId(sheetId)
                .documentId(DOC_ID)
                .build();
    }

    private StoredSharedItem createSharedItem() {
        return StoredSharedItem.builder()
                .documentId(DOC_ID)
                .build();
    }

    private SharedDocument createSharedDoc(Map<Long, List<Long>> sheetCellIds) {
        return SharedDocument.builder()
                .documentId(DOC_ID)
                .sheetCellIds(sheetCellIds)
                .build();
    }
}