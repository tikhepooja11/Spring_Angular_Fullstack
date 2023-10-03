package com.example.springbootfirstfullstack.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootfirstfullstack.entity.Employee;
import com.example.springbootfirstfullstack.exception.ResourceNotFoundException;
import com.example.springbootfirstfullstack.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
	
	private final Logger LOGGER = 
			org.slf4j.LoggerFactory.getLogger(Employee.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	//list all employees
	@GetMapping("/employees")
	public List <Employee> ListAllEmployees(){
		return this.employeeRepository.findAll();		
	}
	
	//create employee rest api
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		return this.employeeRepository.save(employee);
	}
	 
	//fetch employee by id rest api
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :"+ id));
		return ResponseEntity.ok(employee);
	}
	
	
	@GetMapping("/employees/email/{emailId}")
	public Employee getEmpByemailId(@PathVariable String emailId)
	{
		//LOGGER.trace("Entering into method getEmpByEmailId()");//for tracing
		//LOGGER.debug("Checking employee returned with specified email or not ");//for mentioning purpose what i am going to do. we need to enable debug & trace
		Employee employee = employeeRepository.getEmpByEmailId(emailId);
		if(employee == null) {
			LOGGER.error("Sorry employee with this email not found");
		}else {
			LOGGER.info("Employee found...!");

		}
		return this.employeeRepository.getEmpByEmailId(emailId);
	}
	
	//list all employees
		@GetMapping("/employees/departments")
		public void  ListAllEmployeesWithDepartment(){
			System.out.println("hii pooja");
			List<Employee> allEmployees =  this.employeeRepository.findAll();		
			System.out.println(allEmployees.toString());
			
			Map<String, List<Employee>> list1 = new HashMap<>();
			for(int i=0;i<allEmployees.size();i++)
			{
				Employee employee= allEmployees.get(i);
				List<Employee>employees = list1.get(employee.getDepartment());
				if(employees == null) {
					employees=new ArrayList<>();
				}
				employees.add(employee);
				list1.put(employee.getDepartment(), employees);
			}
			System.out.println(list1);
				
			
		}
	
	//update employee rest api
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updatEmployee(@PathVariable Long id, @RequestBody Employee newEmployee) {
		Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("employee not exist with this id :"+id));
		employee.setFirstName(newEmployee.getFirstName());
		employee.setLastName(newEmployee.getLastName());
		employee.setEmailId(newEmployee.getEmailId());
		Employee updatedEmployee = this.employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}
	
	//delete employee rest api
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable long id){
		Employee employee = this.employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("employee not exist with this id :"+id));
		employeeRepository.delete(employee);
		Map<String, Boolean> responseMap = new HashMap<>();
		responseMap.put("delete", Boolean.TRUE);
		return ResponseEntity.ok(responseMap);
	}

}
