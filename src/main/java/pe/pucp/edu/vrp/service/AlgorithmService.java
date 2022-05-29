package pe.pucp.edu.vrp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.pucp.edu.vrp.request.AlgorithmRequest;

@Service
public interface AlgorithmService {

    ResponseEntity<?> routeTrucks(AlgorithmRequest request);
}
