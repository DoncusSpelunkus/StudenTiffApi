package dk.dsa.wl.studentiffapi.services;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface TiffService {
    List<Resource> getAllFiles() throws IOException;
    List<Resource> getFilesByOffsetAndLimit(int offset, int limit) throws IOException;
    List<Resource> getANumberOfRandomFiles(int number) throws IOException;
    List<Resource> getARandomFile() throws IOException;
    List<Resource> getSpecificFile(int number) throws IOException;
    Integer getTotalCount() throws IOException;
}
