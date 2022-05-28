package pe.pucp.edu.vrp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.pucp.edu.vrp.request.AlgorithmRequest;
import pe.pucp.edu.vrp.response.AlgorithmResponse;

@Service
public interface AlgorithmService {

    ResponseEntity<AlgorithmResponse> routeTrucks(AlgorithmRequest request);
}
