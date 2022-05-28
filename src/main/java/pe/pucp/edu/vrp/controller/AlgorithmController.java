package pe.pucp.edu.vrp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.pucp.edu.vrp.request.AlgorithmRequest;
import pe.pucp.edu.vrp.response.AlgorithmResponse;
import pe.pucp.edu.vrp.service.AlgorithmService;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {

    @Autowired
    private AlgorithmService algorithmService;

    @PostMapping("/route")
    public ResponseEntity<AlgorithmResponse> route(@RequestBody AlgorithmRequest request) {
        return algorithmService.routeTrucks(request);
    }
}
