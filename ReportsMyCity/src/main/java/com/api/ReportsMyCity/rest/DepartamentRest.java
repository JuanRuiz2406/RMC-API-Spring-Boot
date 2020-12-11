package com.api.ReportsMyCity.rest;

import com.api.ReportsMyCity.entity.DepartamentMunicipality;
import com.api.ReportsMyCity.reposity.DepartamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("departament")
public class DepartamentRest {

    @Autowired
    private DepartamentRepository departmentRepo;

    // Msjs

    @GetMapping// departments/
    public ResponseEntity<List<DepartamentMunicipality>> getDepartments(){

        List<DepartamentMunicipality> departaments = departmentRepo.findAll();
        return ResponseEntity.ok(departaments);
    }

    @RequestMapping(value = "{depaID}") // departments/{depaId}/
    public ResponseEntity<DepartamentMunicipality> getDepaByI(@PathVariable("depaID") int depaID){

        Optional<DepartamentMunicipality> optionalDepartament = departmentRepo.findById(depaID);

        if(optionalDepartament.isPresent()) {
            return ResponseEntity.ok(optionalDepartament.get());
        }else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<DepartamentMunicipality> createDepartament(@RequestBody DepartamentMunicipality depa){

        DepartamentMunicipality newDepartmentMunicipality = departmentRepo.save(depa);
        return ResponseEntity.ok(newDepartmentMunicipality);
    }

    @DeleteMapping(value = "{depaId}")
    public ResponseEntity<DepartamentMunicipality> deleteDepartment(@PathVariable("depaId") int depaId){

        departmentRepo.deleteById(depaId);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<DepartamentMunicipality> updateDepartament(@RequestBody DepartamentMunicipality depa){

        Optional<DepartamentMunicipality> optionalDepartment = departmentRepo.findById(depa.getId());

        if(optionalDepartment.isPresent()) {
            DepartamentMunicipality updateDepartment = optionalDepartment.get();
            updateDepartment.setDescription(depa.getDescription());
            updateDepartment.setEmail(depa.getEmail());
            updateDepartment.setName(depa.getName());
            updateDepartment.setSchedule(depa.getSchedule());
            updateDepartment.setTelephone(depa.getTelephone());

            departmentRepo.save(updateDepartment);
            return ResponseEntity.ok(updateDepartment);
        }else {
            return ResponseEntity.notFound().build();

        }
    }
}
