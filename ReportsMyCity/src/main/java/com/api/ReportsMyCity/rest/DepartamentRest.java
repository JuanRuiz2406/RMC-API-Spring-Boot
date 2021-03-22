package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.DepartamentMunicipality;
import com.api.ReportsMyCity.repository.DepartamentRepository;
import com.api.ReportsMyCity.security.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("departament")
public class DepartamentRest {

    private final DepartamentRepository departamentRepository;

    public DepartamentRest(DepartamentRepository departamentRepository) {
        this.departamentRepository = departamentRepository;
    }

    // Faltan Msjs

    @GetMapping
    public ResponseEntity<List<DepartamentMunicipality>> getAll(){

        List<DepartamentMunicipality> departaments = departamentRepository.findAll();
        return ResponseEntity.ok(departaments);
    }

    @GetMapping(value = "/byId/{departamentId}")
    public ResponseEntity<DepartamentMunicipality> getById(@PathVariable("departamentId") int departamentId){

        Optional<DepartamentMunicipality> departamentById = departamentRepository.findById(departamentId);

        if(departamentById.isPresent()) {
            return ResponseEntity.ok(departamentById.get());
        }else {
            return new ResponseEntity(new Message("No existe departamento con ese id", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<DepartamentMunicipality> create(@RequestBody DepartamentMunicipality departament){

        DepartamentMunicipality newDepartament = departamentRepository.save(departament);
        return ResponseEntity.ok(newDepartament);
    }

    @PutMapping
    public ResponseEntity<DepartamentMunicipality> update(@RequestBody DepartamentMunicipality departamentChanges){

        Optional<DepartamentMunicipality> existingDepartament = departamentRepository.findById(departamentChanges.getId());

        if(existingDepartament.isPresent()) {
            DepartamentMunicipality updateDepartament = existingDepartament.get();
            updateDepartament.setDescription(departamentChanges.getDescription());
            updateDepartament.setEmail(departamentChanges.getEmail());
            updateDepartament.setName(departamentChanges.getName());
            updateDepartament.setSchedule(departamentChanges.getSchedule());
            updateDepartament.setTelephone(departamentChanges.getTelephone());

            departamentRepository.save(updateDepartament);
            return ResponseEntity.ok(updateDepartament);
        }else {
            return ResponseEntity.notFound().build();

        }
    }

    @DeleteMapping(value = "{departamentId}")
    public ResponseEntity<DepartamentMunicipality> delete(@PathVariable("departamentId") int departamentId){

        departamentRepository.deleteById(departamentId);
        return ResponseEntity.ok(null);
    }

}
