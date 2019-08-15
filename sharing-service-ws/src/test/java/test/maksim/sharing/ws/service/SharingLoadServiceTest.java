package test.maksim.sharing.ws.service;

import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.ws.builder.SharedDocumentBuilder;
import test.maksim.sharing.ws.repository.SharingRepository;
import test.maksim.sharing.ws.repository.domain.StoredSharedItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SharingLoadServiceTest {

    @InjectMocks
    private SharingLoadService service;

    @Mock
    private SharingRepository sharingRepository;
    @Mock
    private SharedDocumentBuilder sharedDocumentBuilder;

    @Test
    public void load_shouldCallRepositoryAndThenBuilder() {
        Set<Long> shareeIds = Set.of(1L);
        var docId = "docId";
        var expectedSharedItem = mock(StoredSharedItem.class);
        var expectedSharedDoc = mock(SharedDocument.class);
        when(sharingRepository.load(anyString(), anySet())).thenReturn(Set.of(expectedSharedItem));
        when(sharedDocumentBuilder.build(anySet())).thenReturn(Optional.of(expectedSharedDoc));

        Optional<SharedDocument> sharedDocument = service.load(docId, shareeIds);

        assertThat(sharedDocument.isPresent(), is(true));
        assertThat(sharedDocument.get(), equalTo(expectedSharedDoc));
        verify(sharingRepository).load(docId, shareeIds);
        verify(sharedDocumentBuilder).build(Set.of(expectedSharedItem));
    }
}