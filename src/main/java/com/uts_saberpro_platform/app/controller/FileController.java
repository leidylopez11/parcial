package com.uts_saberpro_platform.app.controller;

import com.uts_saberpro_platform.app.model.Resolucion;
import com.uts_saberpro_platform.app.repository.ResolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Autowired
    private ResolucionRepository resolucionRepository;

    @GetMapping("/ver-resolucion/{id}")
    @ResponseBody
    public ResponseEntity<Resource> verResolucion(@PathVariable Long id) {
        try {
            Resolucion resolucion = resolucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resolución no encontrada"));
            
            // Extraer solo el nombre del archivo de la URL
            String archivoUrl = resolucion.getArchivoUrl();
            String fileName = archivoUrl.substring(archivoUrl.lastIndexOf("/") + 1);
            
            // Construir la ruta completa al archivo
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se encontró el archivo: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al servir el archivo: " + e.getMessage());
        }
    }
}