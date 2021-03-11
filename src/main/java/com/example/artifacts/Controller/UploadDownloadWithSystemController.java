package com.example.artifacts.Controller;

import com.example.artifacts.Entity.Dto.FileDownloadDTO;
import com.example.artifacts.Entity.Dto.FileUploadDTO;
import com.example.artifacts.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(path="/file")
public class UploadDownloadWithSystemController {


    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping(path = "/addProduct",
            consumes = {"multipart/form-data", "application/json"})
    public String addProducts(@ModelAttribute FileUploadDTO fileUploadDT) throws IOException {

        String returns = fileStorageService.saveProduct(fileUploadDT);
        return returns;

    }


    @GetMapping(path="/getAll")
    public List<FileDownloadDTO> getAll() throws IOException {

        List<FileDownloadDTO>  fileUploadDTO = fileStorageService.getAll();

        return fileUploadDTO;
    }

//
//    @PostMapping(path = "/upload/file")
//    FileUploadDTO singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
//
//        String fileName = fileStorageService.storeFile(file);
//
//        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/download/")
//                .path(fileName)
//                .toUriString();
//
//        String contentType = file.getContentType();
//
//
//        FileUploadDTO response=new FileUploadDTO(fileName, contentType , url);
//
//        return response;
//    }
//
//    @GetMapping(path="/getImage/{id}")
//    public ResponseEntity<Resource> getFile(@PathVariable Integer id) throws IOException {
//
//        FileUploads resource=fileStorageService.getAllImage(id);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(resource.getAdds()))
////                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+resource.getFilename())
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=/"+resource.getName()+"/")
//                .body(new ByteArrayResource(resource.getSize()));
//    }
//
    @GetMapping(path = "/download/{fileName}")
    ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName, HttpServletRequest request){

        Resource resource=fileStorageService.downloadFile(fileName);

//        MediaType contentType= MediaType.valueOf(MediaType.ALL_VALUE);
//        MediaType contentType = MediaType.IMAGE_JPEG;
        String mineType = null;
        try {
            mineType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mineType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+resource.getFilename())
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .body(resource);

    }

    @GetMapping(path = "/downloadImage/{fileName}")
    ResponseEntity<Resource> downloadImage(@PathVariable String fileName, HttpServletRequest request){

        Resource resource=fileStorageService.downloadFile(fileName);

//        MediaType contentType= MediaType.valueOf(MediaType.ALL_VALUE);
//        MediaType contentType = MediaType.IMAGE_JPEG;
        String mineType = null;
        try {
            mineType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mineType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .body(resource);

    }

    @DeleteMapping(path = "/delete/{id}")
    public String deleteFile(@PathVariable("id") Integer id) throws IOException {

        String resturnValue = fileStorageService.deleteFile(id);

        return resturnValue;
    }

}

