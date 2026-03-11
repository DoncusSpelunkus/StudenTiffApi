package dk.dsa.wl.studentiffapi.mockdao;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TiffMockDaoImpl implements TiffMockDao{

    @Override
    public List<Resource> getFiles(Integer offset, Integer limit) {
        List<Resource> pdfList = new ArrayList<>();
        for(int i = offset+1; i<=offset+limit; i++){
            String fileName = i + ".tiff";
            ClassPathResource resource = new ClassPathResource("TiffFiles/" + fileName);
            if(!resource.exists()){
                continue;
            }
            pdfList.add(resource);
        }
        if (pdfList.isEmpty()) {
            throw new IllegalArgumentException("No files found");
        }
        return pdfList;
    }

    @Override
    public Integer getTotalCount() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:TiffFiles/*");
        return resources.length;
    }
}
