package com.oocl.springbootemployee.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeAgeSalaryNotMatchedException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeInMemoryRepository;

import java.util.List;

import com.oocl.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    EmployeeInMemoryRepository mockedEmployeeInMemoryRepository;

    @Mock
    EmployeeRepository mockEmployeeRepository;

    @InjectMocks
    EmployeeService mockEmployeeService;


    @Test
    void should_return_the_given_employees_when_getAllEmployees() {
        //given
        when(mockEmployeeRepository.findAll()).thenReturn(List.of(new Employee(1, "Lucy", 18, Gender.FEMALE, 8000.0)));


        //when
        List<Employee> allEmployees = mockEmployeeService.findAll();

        //then
        assertEquals(1, allEmployees.size());
        assertEquals("Lucy", allEmployees.get(0).getName());
    }

    @Test
    void should_return_the_created_employee_when_create_given_a_employee() {
        //given
        Employee lucy = new Employee(1, "Lucy", 18, Gender.FEMALE, 8000.0);
        when(mockedEmployeeInMemoryRepository.create(any())).thenReturn(lucy);


        //when
        Employee createdEmployee = mockEmployeeService.create(lucy);

        //then
        assertEquals("Lucy", createdEmployee.getName());
    }

    @Test
    void should_throw_EmployeeAgeNotValidException_when_create_given_a_employee_with_age_17() {
        //given
        Employee kitty = new Employee(1, "Kitty", 6, Gender.FEMALE, 8000.0);
        //when
        //then
        assertThrows(EmployeeAgeNotValidException.class, () -> mockEmployeeService.create(kitty));
        verify(mockedEmployeeInMemoryRepository, never()).create(any());
    }

    @Test
    void should_throw_EmployeeAgeNotValidException_when_create_given_a_employee_with_age_66() {
        //given
        Employee kitty = new Employee(1, "Kitty", 66, Gender.FEMALE, 8000.0);

        //when
        //then
        assertThrows(EmployeeAgeNotValidException.class, () -> mockEmployeeService.create(kitty));
        verify(mockedEmployeeInMemoryRepository, never()).create(any());
    }

    @Test
    void should_created_employee_active_when_create_employee() {
        //given

        Employee lucy = new Employee(1, "Lucy", 18, Gender.FEMALE, 8000.0);
        //when
        mockEmployeeService.create(lucy);
        /* then */
        verify(mockedEmployeeInMemoryRepository).create(argThat(Employee::getActive));
    }

    @Test
    void should_throw_EmployeeAgeSalaryNotMatchedException_when_save_given_a_employee_with_age_over_30_and_salary_below_20K() {
        //given
        Employee bob = new Employee(1, "Bob", 31, Gender.FEMALE, 8000.0);

        //when
        //then
        assertThrows(EmployeeAgeSalaryNotMatchedException.class, () -> mockEmployeeService.create(bob));
        verify(mockedEmployeeInMemoryRepository, never()).create(any());
    }

    @Test
    void should_throw_EmployeeInactiveException_when_update_inactive_employee() {
        //given
        Employee inactiveEmployee = new Employee(1, "Bob", 31, Gender.FEMALE, 8000.0);
        inactiveEmployee.setActive(false);
        when(mockedEmployeeInMemoryRepository.findById(1)).thenReturn(inactiveEmployee);

        //when
        //then
        assertThrows(EmployeeInactiveException.class, () -> mockEmployeeService.update(1, inactiveEmployee));
        verify(mockedEmployeeInMemoryRepository, never()).create(any());
    }
}
