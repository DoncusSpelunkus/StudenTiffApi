package dk.dsa.wl.studentiffapi.rest;

import dk.dsa.wl.studentiffapi.services.TiffService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController("/rest/tiff/")
@RequiredArgsConstructor
public class TiffController {

    private final TiffService tiffService;

    @GetMapping(value = "getFiles/{offset}/{limit}")
    @Operation(
            summary = "Fetches tiff files from app memory by offset and limit",
            description = "Offset and limit are both subject to change in terms of limitation."
    )
    public ResponseEntity getFilesByOffsetAndLimit(@PathVariable int offset, @PathVariable int limit) throws IOException {
        try {
            List<Resource> resources = tiffService.getFilesByOffsetAndLimit(offset, limit);

            ByteArrayOutputStream out = getZipOutputStream(resources);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tiffs.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server error " + e.getMessage());
        }
    }

    @GetMapping(value = "getAllFiles")
    @Operation(
            summary = "Fetches all tiff files from app memory",
            description = "This operation could be slow depending on current amount of files in memory"
    )
    public ResponseEntity getAllFiles() {
        try {
            List<Resource> resources = tiffService.getAllFiles();

            ByteArrayOutputStream out = getZipOutputStream(resources);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tiffs.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server error " + e.getMessage());
        }
    }

    @GetMapping(value = "getFiles/{amount}")
    @Operation(
            summary = "Fetches a certain amount of random tiff files from app memory",
            description = "If amount exceeds total in memory it will return all."
    )
    public ResponseEntity getANumberOfRandomFiles(@PathVariable int amount) throws IOException {
        try {
            List<Resource> resources = tiffService.getANumberOfRandomFiles(amount);

            ByteArrayOutputStream out = getZipOutputStream(resources);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tiffs.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server error " + e.getMessage());
        }
    }

    @GetMapping(value = "getRandomFile")
    @Operation(
            summary = "Fetches a random tiff file from app memory"
    )
    public ResponseEntity getARandomFile() {
        try {
            List<Resource> resources = tiffService.getARandomFile();

            ByteArrayOutputStream out = getZipOutputStream(resources);
            System.out.println(resources.size() );
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tiffs.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server error " + e.getMessage());
        }
    }

    private ByteArrayOutputStream getZipOutputStream(List<Resource> resources) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Resource resource : resources) {
                ZipEntry entry = new ZipEntry(resource.getFilename());
                zos.putNextEntry(entry);
                zos.write(resource.getInputStream().readAllBytes());
                zos.closeEntry();
            }
        }

        return baos;
    }

    @GetMapping("/getCount")
    @Operation(
            description = "Gets the total count of tiff files in memory"
    )
    public Integer getCount() throws IOException {
        return tiffService.getTotalCount();
    }

    @GetMapping("/getById/{id}")
    @Operation(
            description = "Gets a specific tiff for testing"
    )
    public ResponseEntity getById(@PathVariable Integer id) throws IOException {
        try {
            List<Resource> resources = tiffService.getSpecificFile(id);

            ByteArrayOutputStream out = getZipOutputStream(resources);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tiffs.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server error " + e.getMessage());
        }
    }

}
