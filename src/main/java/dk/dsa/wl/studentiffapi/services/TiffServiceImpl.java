package dk.dsa.wl.studentiffapi.services;

import dk.dsa.wl.studentiffapi.mockdao.TiffMockDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TiffServiceImpl implements TiffService{

    private final TiffMockDao tiffMockDao;

    @Override
    public List<Resource> getAllFiles() throws IOException {
        Integer totalCount = tiffMockDao.getTotalCount();
        return tiffMockDao.getFiles(0, totalCount);
    }

    @Override
    public List<Resource> getFilesByOffsetAndLimit(int offset, int limit) throws IOException {
        return tiffMockDao.getFiles(offset, limit);
    }

    @Override
    public List<Resource> getANumberOfRandomFiles(int amount) throws IOException {
        Integer totalCount = tiffMockDao.getTotalCount();
        int offSetLimit = totalCount - amount;
        int offset = RandomUtils.secure().randomInt(0, offSetLimit);
        return tiffMockDao.getFiles(offset, amount);
    }

    @Override
    public List<Resource> getARandomFile() throws IOException {
        Integer totalCount = tiffMockDao.getTotalCount();
        int randomInt = RandomUtils.secure().randomInt(0, totalCount);
        return tiffMockDao.getFiles(randomInt, 1);
    }

    @Override
    public List<Resource> getSpecificFile(int number) {
        if(number == 0){
            throw new IllegalArgumentException("id must greater than 0");
        }
        return tiffMockDao.getFiles(number - 1, 1);
    }

    @Override
    public Integer getTotalCount() throws IOException {
        return tiffMockDao.getTotalCount();
    }


    // Keep in case we get pdf files we wanna convert?
    private List<Resource> convertToTiffs(List<Resource> pdfList) throws IOException {
        List<Resource> tiffList = new ArrayList<>();

        for(Resource pdf : pdfList) {
            try (PDDocument document = Loader.loadPDF(pdf.getInputStream().readAllBytes())) {
                PDFRenderer renderer = new PDFRenderer(document);
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    BufferedImage image = renderer.renderImageWithDPI(i, 300);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "TIFF", baos);

                    int pageNumber = i + 1;
                    ByteArrayResource tiffResource = new ByteArrayResource(baos.toByteArray()) {
                        @Override
                        public String getFilename() {
                            return "page-" + pageNumber + ".tiff";
                        }
                    };

                    tiffList.add(tiffResource);
                }
            }
        }
        return tiffList;
    }


}
