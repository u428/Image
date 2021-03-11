package com.example.artifacts.Repository;

import com.example.artifacts.Entity.FileUploads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileUploads, Integer> {


}
