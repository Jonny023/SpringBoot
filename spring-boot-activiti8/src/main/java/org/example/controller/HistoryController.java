package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.example.service.ProcessDiagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.InputStream;

@Controller
public class HistoryController {

    @Autowired
    private ProcessDiagramService diagramService;

    @SneakyThrows
    @GetMapping("/process-instances/{id}/diagram")
    public ResponseEntity<byte[]> getProcessDiagram(@PathVariable("id") String id, HttpServletResponse response) {
        try (InputStream diagram = diagramService.generateDiagram(id)) {
            if (diagram != null) {
                // FileUtil.writeBytes(StreamUtils.copyToByteArray(diagram), new File("./a.svg"));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("image/svg+xml"));
                return new ResponseEntity<>(StreamUtils.copyToByteArray(diagram), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}