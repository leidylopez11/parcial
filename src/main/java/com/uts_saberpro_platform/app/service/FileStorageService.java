package com.uts_saberpro_platform.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String guardarArchivo(MultipartFile archivo) throws IOException {
        // Crear el directorio si no existe (ruta relativa al proyecto)
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Obtener la extensión del archivo
        String originalFilename = archivo.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        // Generar nombre único para el archivo
        String nombreArchivo = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(nombreArchivo);
        
        // Guardar el archivo
        Files.copy(archivo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Devolver la URL web (NO la ruta del sistema de archivos)
        return "/uploads/resoluciones/" + nombreArchivo;
    }
}