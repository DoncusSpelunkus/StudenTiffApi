package dk.dsa.wl.studentiffapi.mockdao;

import org.springframework.core.io.Resource;


import java.io.IOException;
import java.util.List;

public interface TiffMockDao {
    List<Resource> getFiles(Integer offset, Integer limit);
    Integer getTotalCount() throws IOException;
}
