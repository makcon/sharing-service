package test.maksim.sharing.ws.repository;

import test.maksim.sharing.ws.repository.domain.SharedItem;
import test.maksim.sharing.ws.repository.domain.StoredSharedItem;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class InMemorySharingRepositoryTest {

    private static final String DOC_ID_1 = "docId1";
    private static final String DOC_ID_2 = "docId2";
    private static final long SHEET_ID_1 = 1;
    private static final long SHEET_ID_2 = 2;
    private static final long CELL_ID_1 = 11;
    private static final long CELL_ID_2 = 12;
    private static final long SHAREE_ID_1 = 111;
    private static final long SHAREE_ID_2 = 112;

    private InMemorySharingRepository repository;

    @Before
    public void setUp() {
        repository = new InMemorySharingRepository();
    }

    @Test
    public void deleteDocument_shouldDeleteDoc1Only() {
        var item1 = createSharedItem(SHAREE_ID_1, DOC_ID_2, null, null);
        var item2 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_1, null);
        var item3 = createSharedItem(SHAREE_ID_1, DOC_ID_1, null, null);
        var item4 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_2, CELL_ID_1);
        repository.save(List.of(item1, item2, item3, item4));

        repository.deleteDocument(DOC_ID_1, List.of(SHAREE_ID_1, SHAREE_ID_2));

        assertThat(repository.load(DOC_ID_1, List.of(SHAREE_ID_1, SHAREE_ID_2)), hasSize(0));
        assertThat(repository.load(DOC_ID_2, List.of(SHAREE_ID_1, SHAREE_ID_2)), hasSize(1));
    }

    @Test
    public void deleteSheets_shouldDelete2Sheets() {
        var item1 = createSharedItem(SHAREE_ID_1, DOC_ID_2, SHEET_ID_1, null);
        var item2 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_1, null);
        var item3 = createSharedItem(SHAREE_ID_1, DOC_ID_1, SHEET_ID_2, null);
        var item4 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_2, CELL_ID_1);
        var item5 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_1, CELL_ID_1);
        repository.save(List.of(item1, item2, item3, item4, item5));

        repository.deleteSheets(DOC_ID_1, List.of(SHEET_ID_1), List.of(SHAREE_ID_1, SHAREE_ID_2));

        assertThat(repository.load(DOC_ID_1, List.of(SHAREE_ID_1, SHAREE_ID_2)), hasSize(2));
        assertThat(repository.load(DOC_ID_2, List.of(SHAREE_ID_1, SHAREE_ID_2)), hasSize(1));
    }

    @Test
    public void deleteCells_shouldDelete3Cells() {
        var item1 = createSharedItem(SHAREE_ID_1, DOC_ID_2, SHEET_ID_1, CELL_ID_1);
        var item2 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_1, CELL_ID_1);
        var item3 = createSharedItem(SHAREE_ID_1, DOC_ID_1, SHEET_ID_2, CELL_ID_2);
        var item4 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_2, CELL_ID_1);
        var item5 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_1, CELL_ID_1);
        repository.save(List.of(item1, item2, item3, item4, item5));

        repository.deleteCells(DOC_ID_1, List.of(CELL_ID_1), List.of(SHAREE_ID_1, SHAREE_ID_2));

        assertThat(repository.load(DOC_ID_1, List.of(SHAREE_ID_1, SHAREE_ID_2)), hasSize(1));
        assertThat(repository.load(DOC_ID_2, List.of(SHAREE_ID_1, SHAREE_ID_2)), hasSize(1));
    }

    @Test
    public void load_shouldLoadByDocIdForSharee() {
        var item1 = createSharedItem(SHAREE_ID_1, DOC_ID_2, SHEET_ID_1, CELL_ID_1);
        var item2 = createSharedItem(SHAREE_ID_1, DOC_ID_1, SHEET_ID_1, CELL_ID_1);
        var item3 = createSharedItem(SHAREE_ID_1, DOC_ID_1, SHEET_ID_2, null);
        var item4 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_1, CELL_ID_1);
        repository.save(List.of(item1, item2, item3, item4));

        Set<StoredSharedItem> sharedItems = repository.load(DOC_ID_1, List.of(SHAREE_ID_1));

        assertThat(sharedItems, containsInAnyOrder(createStoredSharedItem(SHEET_ID_1, CELL_ID_1), createStoredSharedItem(SHEET_ID_2, null)));
    }

    @Test
    public void load_shouldLoadUniqueItems() {
        var item1 = createSharedItem(SHAREE_ID_1, DOC_ID_1, SHEET_ID_1, CELL_ID_1);
        var item2 = createSharedItem(SHAREE_ID_2, DOC_ID_1, SHEET_ID_1, CELL_ID_1);
        repository.save(List.of(item1, item2));

        Set<StoredSharedItem> sharedItems = repository.load(DOC_ID_1, List.of(SHAREE_ID_1, SHAREE_ID_2));

        assertThat(sharedItems, equalTo(Set.of(createStoredSharedItem(SHEET_ID_1, CELL_ID_1))));
    }

    // Util methods

    private SharedItem createSharedItem(Long shareeId,
                                        String docId,
                                        Long sheetId,
                                        Long cellId) {
        return SharedItem.builder()
                .shareeId(shareeId)
                .documentId(docId)
                .cellId(cellId)
                .sheetId(sheetId)
                .build();
    }

    private StoredSharedItem createStoredSharedItem(Long sheetId,
                                                    Long cellId) {
        return StoredSharedItem.builder()
                .documentId(DOC_ID_1)
                .cellId(cellId)
                .sheetId(sheetId)
                .build();
    }
}