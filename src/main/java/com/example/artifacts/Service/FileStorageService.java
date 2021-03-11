package com.example.artifacts.Service;

import com.example.artifacts.Entity.Dto.FileDownloadDTO;
import com.example.artifacts.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import com.example.artifacts.Entity.FileUploads;
import com.example.artifacts.Entity.Dto.FileUploadDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileStorageService {

    @Autowired
    public FileRepository fileRepository;

    private Path fileStoragePath;
    private String fileStorageLocation;


    public FileStorageService(@Value("java_code") String fileStorageLocation){

        this.fileStorageLocation=fileStorageLocation;

        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public String storeFile(MultipartFile file) throws IOException {
//
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//        System.out.println(fileName  +"   ===    Service   42 ");
//
//        Path filePath = Paths.get(fileStoragePath + "//" + fileName);
//
//        FileUploads fileUploads=new FileUploads();
//
//        fileUploads.setName(file.getOriginalFilename());
//        fileUploads.setAdds(file.getContentType());
//        fileUploads.setSize(file.getBytes());
//
//        fileRepository.save(fileUploads);
//
//        System.out.println(filePath +"    ===   Service   46");
//
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//
//
//        return fileName;
//    }
//
    public Resource downloadFile(String fileName) {

        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource = null;
        try {
            resource=  new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (resource.exists() && resource.isReadable()){
            return resource;
        }else throw new RuntimeException("the file does not Exist");
    }


    public String saveProduct(FileUploadDTO fileUploadDTO) throws IOException {

        FileUploads fileUpload=new FileUploads();

        fileUpload.setCosts(fileUploadDTO.getCosts());
        fileUpload.setDescription(fileUploadDTO.getDescription());
        fileUpload.setName(fileUploadDTO.getName());

        fileUpload.setFileName(fileUploadDTO.getFiles().getOriginalFilename());

        fileRepository.save(fileUpload);


        String AA=fileUploadDTO.getFiles().getOriginalFilename();
        String fileName = String.valueOf(fileUpload.getId())+AA.substring(AA.length()-4, AA.length());
        fileUpload.setFileName(fileName);
        FileUploads fileUpload1 = fileRepository.save(fileUpload);

        Path filePath = Paths.get(fileStoragePath + "//" + fileName);


        Files.copy(fileUploadDTO.getFiles().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        if (fileUpload1.getName()!=null){
            return "Success";
        }

        return "Error";

    }

    public List<FileDownloadDTO> getAll() throws IOException {
        List<FileDownloadDTO> list=new ArrayList<>();
        List<FileUploads> fileUploads=fileRepository.findAll();

        for (FileUploads x: fileUploads){
            FileDownloadDTO fileDownloadDTO=new FileDownloadDTO();
            Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(x.getFileName());
            Resource resource=  new UrlResource(path.toUri());

            fileDownloadDTO.setCosts(x.getCosts());
            fileDownloadDTO.setDescription(x.getDescription());
            fileDownloadDTO.setFileName(resource.getFilename());
            fileDownloadDTO.setName(x.getName());
            fileDownloadDTO.setId(x.getId());

            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/file/download/")
                    .path(resource.getFilename())
                    .toUriString();

            fileDownloadDTO.setFilePath(url);



            list.add(fileDownloadDTO);
        }
        return list;
    }


    public String deleteFile(Integer id) throws IOException {
        Optional<FileUploads> fileUploads = fileRepository.findById(id);
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileUploads.get().getFileName());
        Resource resource=  new UrlResource(path.toUri());
        resource.getFile().delete();
        fileRepository.delete(fileUploads.get());

        return "success";

    }


}
